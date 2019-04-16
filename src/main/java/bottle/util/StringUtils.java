package bottle.util;

import java.util.regex.Pattern;

public class StringUtils {
    /**字符串不为空*/
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0 ;
    }

    /**判断一组字符串都不为空*/
    public static boolean isEmpty(String... arr){
        for (String str : arr){
            if (isEmpty(str)) return true;
        }
        return false;
    }
    /** 去除空格 */
    public static String trim(String text) {
        if(text == null || "".equals(text)) {
            return text;
        }
        return text.trim();
    }

}
