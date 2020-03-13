package bottle.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 */

public class GoogleGsonUtil {

    private final static Gson builder =  new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> new JsonPrimitive(src.longValue()+""))
            .registerTypeAdapter(Integer.class,(JsonSerializer<Integer>) (src, typeOfSrc, context) -> new JsonPrimitive(src+""))
            .create();

    /** 判断是否为JSON格式字符串 */
    public static boolean isJsonFormatter(String str) {
        try {
            new JsonParser().parse(str);
        } catch (Exception e) {
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
            return builder.fromJson(json, type);//对于javabean直接给出class实例
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * javabean to json
     * @param object
     * @return
     */
    public static String javaBeanToJson(Object object){
        return builder.toJson(object);
    }

    /**
     * json to javabean
     *
     * @param json
     */
    public static <T> T jsonToJavaBean(String json,Class<T> cls) {
        try {
            if (json==null || json.length()==0) return null;
            return builder.fromJson(json, cls);//对于javabean直接给出class实例
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T,D> HashMap<T,D> string2Map(String json){
        try {
            if (json==null || json.length()==0) return null;
            return jsonToJavaBean(json, new TypeToken<HashMap<T,D>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> json2List(String json,Class<T> clazz){
        List<T> list = new ArrayList<>();
        try {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement element : array) {
                list.add(gson.fromJson(element, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (JSONException ignored) { }
        return false;
    }

    public static int convertInt(Object val){
        return new BigDecimal(String.valueOf(val)).intValue();
    }

}
