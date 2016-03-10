package cc.trity.library.utils;

import android.text.TextUtils;

/**
 * 暂时用这个类来代替一些数据的处理，以后再区分
 * Created by TryIT on 2016/3/1.
 */
public class Utils {
    private static final String defaultStr="";
    private static final int defaultInteger=-1;

    public static String safeSubString(String str,int start,int end){
        if(TextUtils.isEmpty(str)){
            return defaultStr;
        }
        if(start<0){
            return defaultStr;
        }
        if(end>start){
            return str.substring(start,end);
        }
        return defaultStr;
    }

    public static String safeSubString(String str,int start){
        if(TextUtils.isEmpty(str)){
            return defaultStr;
        }
        if(start<0){
            return defaultStr;
        }
        return str.substring(start);
    }

    /**
     *
     * @Title: convertToInt
     * @Description: 对象转化为整数数字类型，转化成非浮点型的时候需要特别注意
     * @param value
     * @return integer
     * @throws
     */
    public final static int convertToInt(Object value) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultInteger;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultInteger;
            }
        }
    }

    public final static long convertToLong(Object value) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultInteger;
        }
        try {
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).longValue();
            } catch (Exception e1) {
                return defaultInteger;
            }
        }
    }
}
