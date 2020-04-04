package bottle.util;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 */

public class GoogleGsonUtil {

    private static void printJsonError(String jsonText,Throwable e){
        Log4j.error("GSON-解析异常,内容:\t"+jsonText,e);
    }

    private final static Gson builderLong2String =  new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
//            .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> new JsonPrimitive(src.longValue()+""))
//            .registerTypeAdapter(Float.class, (JsonSerializer<Float>) (src, typeOfSrc, context) -> new JsonPrimitive(src.longValue()+""))
//            .registerTypeAdapter(Integer.class,(JsonSerializer<Integer>) (src, typeOfSrc, context) -> new JsonPrimitive(src+""))

    /** 判断是否为JSON格式字符串 */
    public static boolean isJsonFormatter(String json) {
        try {
            new JsonParser().parse(json);
        } catch (Exception e) {
            printJsonError(json,e);
            return false;
        }
        return true;
    }

    /**
     * json to javabean
     *new TypeToken<List<xxx>>(){}.getType()
     * @param json
     */
    public static <T> T jsonToJavaBean(String json,Type type) {
        try {
            if (json==null || json.length()==0) return null;
            return builderLong2String.fromJson(json, type);//对于javabean直接给出class实例
        } catch (Exception e) {
            printJsonError(json,e);
        }
        return null;
    }

    /**
     * javabean to json
     * @param object
     * @return
     */
    public static String javaBeanToJson(Object object){
        try {
            if (object!=null){
                return builderLong2String.toJson(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json to javabean
     *
     * @param json
     */
    public static <T> T jsonToJavaBean(String json,Class<T> cls) {
        try {
            if (json==null || json.length()==0) return null;
            return builderLong2String.fromJson(json, cls);//对于javabean直接给出class实例
        } catch (Exception e) {
            printJsonError(json,e);
        }
        return null;
    }

    public static <T,D> HashMap<T,D> string2Map(String json){
        try {
            if (json==null || json.length()==0) return null;

            Type type = new TypeToken<HashMap<T,D>>() {}.getType();
            Gson _builder =  new GsonBuilder()
                    .registerTypeAdapter(type,
                            new TypeAdapter<Object>(){
                        @Override
                        public void write(JsonWriter out, Object value) throws IOException {

                        }

                        @Override
                        public Object read(JsonReader in) throws IOException {

                            JsonToken token = in.peek();

                            switch (token) {
                                case STRING:
                                    return in.nextString();
                                case NUMBER:
                                    String s = in.nextString();
                                    if (s.contains(".")) {
                                        return Double.valueOf(s);
                                    } else {
                                        try {
                                            return Integer.valueOf(s);
                                        } catch (Exception e) {
                                            return Long.valueOf(s);
                                        }
                                    }
                                case BOOLEAN:
                                    return in.nextBoolean();
                                case NULL:
                                    in.nextNull();
                                    return null;

                                case BEGIN_ARRAY:
                                    List<Object> list = new ArrayList<>();
                                    in.beginArray();
                                    while (in.hasNext()) {
                                        list.add(read(in));
                                    }
                                    in.endArray();
                                    return list;

                                case BEGIN_OBJECT:
                                    HashMap<T,D> map = new HashMap<>();
                                    in.beginObject();
                                    while (in.hasNext()) {
                                        map.put((T)in.nextName(), (D)read(in));
                                    }
                                    in.endObject();
                                    return map;
                                default:
                                    throw new IllegalStateException();
                            }
                        }
                    } ).create();

              return _builder.fromJson(json, type);//对于javabean直接给出class实例
        } catch (Exception e) {
            printJsonError(json,e);
        }
        return null;
    }

    public static <T> List<T> json2List(String json,Class<T> clazz){
        List<T> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            JsonParser reader = new JsonParser();
            JsonElement jsonElement = reader.parse(json);
            JsonArray array = jsonElement.getAsJsonArray();
            for (JsonElement element : array) {
                list.add(gson.fromJson(element, clazz));
            }
        } catch (Exception e){
            printJsonError(json,e);
        }
        return list;
    }

    /**
     * 判断是否是数组类型的json字符串
     */
    public static boolean checkJsonIsArray(String json){
        try {
            Object jsonObj = new JSONTokener(json).nextValue();
            if (jsonObj instanceof JSONArray) {
                return true;
            }
        } catch (Exception e) {
            printJsonError(json,e);
        }
        return false;
    }

    public static int convertInt(Object val){
        return new BigDecimal(String.valueOf(val)).intValue();
    }

    /** 格式化字符串 */
    public static String toPrettyFormat(Object object) {
        try {
            if (object!=null){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                return gson.toJson(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
