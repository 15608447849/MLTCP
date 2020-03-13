package bottle.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

    /**字符串为空*/
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0 ;
    }

    /**字符串不为空*/
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**判断一组字符串有一个为空 true*/
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

    /**
     * 比较两个字符串（大小写敏感）。
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }


    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtil.isBlank(cs);
    }

    /**
     * @param content 字符串
     * @param charset 字符集
     */
    public static byte[] getContentBytes(String content, String charset) {
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
        }
        return null;
    }

}
