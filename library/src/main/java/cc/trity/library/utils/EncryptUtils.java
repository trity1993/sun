package cc.trity.library.utils;

import android.util.Log;

import java.security.MessageDigest;

/**
 * 加密工具类：3des,md5等
 * Created by TryIT on 2016/2/20.
 */
public class EncryptUtils {
    private static final String TAG="EncryptUtils";

    // MD5相关函数
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd','e', 'f'};
    /**
     * MD5运算
     *
     * @param s
     * @return String 返回密文
     */
    public static String getMd5(final String s){
        try{
            final MessageDigest msgDigest=MessageDigest.getInstance("MD5");
            msgDigest.update(s.getBytes());
            final byte[] messageDigest=msgDigest.digest();
            return toHexString(messageDigest);
        }catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }
    /**
     * 转换为十六进制字符串
     *
     * @param b
     *            byte数组
     * @return String byte数组处理后字符串
     */
    public static String toHexString(final byte[] b){
        final StringBuffer sBuf=new StringBuffer(b.length*2);
        for(final byte element:b){
            sBuf.append(HEX_DIGITS[(element & 0xf0)>>>4]);
            sBuf.append(HEX_DIGITS[(element & 0xf0)]);
        }
        return sBuf.toString();
    }
}
