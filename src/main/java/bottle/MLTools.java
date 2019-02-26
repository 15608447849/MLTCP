package bottle;

public class MLTools {
    public static String version(){
        return "1.0.0";
    }
    //字符串不为空
    public static boolean isEmpty(String str){
        return str==null || str.trim().length() == 0 ;
    }
}
