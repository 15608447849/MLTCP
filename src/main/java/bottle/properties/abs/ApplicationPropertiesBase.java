package bottle.properties.abs;



import bottle.properties.annotations.PropertiesFilePath;
import bottle.properties.annotations.PropertiesName;
import bottle.properties.infs.FieldConvert;
import bottle.properties.infs.baseImp.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;

/**
 * 读取属性文件
 */
public final class ApplicationPropertiesBase {

    private static final HashMap<String, FieldConvert> baseType = new HashMap<>();

    static {
        baseType.put("class java.lang.String",new StringConvertImp());
        baseType.put("boolean",new BooleanConvertImp());
        baseType.put("int",new IntConvertImp());
        baseType.put("float",new FloatConvertImp());
        baseType.put("double",new DoubleConvertImp());
        baseType.put("long",new LongConvertImp());
    }

    private static final Properties properties = new Properties();

    public static String getRuntimeRootPath(Class clazz) {
        try {
            return new File(URLDecoder.decode(clazz.getProtectionDomain().getCodeSource().getLocation().getFile(),"UTF-8")).getParent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //读取配置文件
    private static InputStream readPathProperties(Class clazz,String filePath) throws FileNotFoundException {
        //优先从外部配置文件获取
        String dirPath = getRuntimeRootPath(clazz);
        if (dirPath != null){
            File file = new File(dirPath+"/resources"+filePath);
            if (file.exists()){
                return  new FileInputStream(file);
            }
        }

        return clazz.getResourceAsStream( filePath );
    }

    private static PropertiesFilePath getPropertiesFilePath(Class clazz) {
        PropertiesFilePath annotation = (PropertiesFilePath) clazz.getAnnotation(PropertiesFilePath.class);
        return annotation;
    }

    public static void initStaticFields(Class clazz) {
        try {
            PropertiesFilePath p =  getPropertiesFilePath(clazz);
            if (p==null) throw new RuntimeException("配置文件获取失败,请使用注解 'PropertiesFilePath'");
            String filePath = p.value();
            String charset = p.decode();
            InputStream in = readPathProperties(clazz,filePath);
            if (in==null) throw new RuntimeException("配置文件获取失败: "+ filePath);
            properties.clear();
            properties.load(new InputStreamReader(in,charset));
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                PropertiesName name = field.getAnnotation(PropertiesName.class);
                if (name==null) continue;
                field.setAccessible(true);
                String key = name.value();
                String value = properties.getProperty(key);
                if (value==null || value.length()==0) continue;
                //获取属性类型
                String type = field.getGenericType().toString();
                if(baseType.containsKey(type)){
                    try {
                        baseType.get(type).setValue(clazz,field,value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
