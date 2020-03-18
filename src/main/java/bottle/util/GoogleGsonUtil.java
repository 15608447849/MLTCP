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

    private static class TEST{
        long longValue;
        int intValue;
        String stringValue;
        double doubleValue;
        float floatValue;

        public TEST(long longValue, int intValue, String stringValue, double doubleValue, float floatValue) {
            this.longValue = longValue;
            this.intValue = intValue;
            this.stringValue = stringValue;
            this.doubleValue = doubleValue;
            this.floatValue = floatValue;
        }
    }

    public static void main(String[] args) {
        TEST t = new TEST(1577932202698001040L,200,"hahaha",15.6001d,22.77f);
        String json = GoogleGsonUtil.javaBeanToJson(t);
        System.out.println(json);
        Map<String, Object> map = GoogleGsonUtil.string2Map(json);
        System.out.println(map);

    }



    private final static Gson builderLong2String =  new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING).create();
//            .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> new JsonPrimitive(src.longValue()+""))
//            .registerTypeAdapter(Float.class, (JsonSerializer<Float>) (src, typeOfSrc, context) -> new JsonPrimitive(src.longValue()+""))
//            .registerTypeAdapter(Integer.class,(JsonSerializer<Integer>) (src, typeOfSrc, context) -> new JsonPrimitive(src+""))

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
            return builderLong2String.fromJson(json, type);//对于javabean直接给出class实例
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
        return builderLong2String.toJson(object);
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
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }



    private static class MapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public void write(JsonWriter out, Object value) throws IOException {

        }

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();

            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new HashMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

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

                default:
                    throw new IllegalStateException();
            }
        }
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

    /** 格式化字符串 */
    public static String toPrettyFormat(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

}
