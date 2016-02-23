package cc.trity.library.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Environment;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * 文件操作类
 * Created by TryIT on 2016/1/11.
 */
public class FileUtils {
    private static final String TAG="FileUtils";
    private static final String ERROR_RESPONSE="";

    /**
     * 查询是否有key对应的缓存文件
     *
     * @param keyPath
     * @return
     */
    public static boolean isExistFile(final String keyPath) {
        final File file = new File(keyPath);
        return file.exists();
    }

    /**
     * 判断缓存文件夹(sd/非sd的情况下)可用的容量
     * @param context
     * @return
     */
    public static long getDiskCacheDirSize(Context context){
        String sdCardDirPath=getDiskCacheDirPath(context);
        return new File(sdCardDirPath).getFreeSpace();
    }

    /**
     * @param context
     * @param uniqueName 路径上添加独立的文件夹
     * @return 当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，
     * 否则就调用getCacheDir()方法来获取缓存路径。
     * 前者获取到的就是 /sdcard/Android/data/<application package>/cache 这个路径，
     * 而后者获取到的是 /data/data/<application package>/cache 这个路径。
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * getDiskCacheDir方法的分离，此处得到cache的路径
     * @param context
     * @return
     */
    public static String getDiskCacheDirPath(Context context){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getAssetsData(Context context,String fileName){
        if(context==null)
            return ERROR_RESPONSE;
        try( InputStream is=context.getAssets().open(fileName);
             BufferedReader bufReader=new BufferedReader(new InputStreamReader(is))) {
            String line=null;
            StringBuffer strBuf=new StringBuffer();
            while ((line=bufReader.readLine())!=null){
                strBuf.append(line);
            }

            return strBuf.toString();
        } catch (IOException e) {
           LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return ERROR_RESPONSE;
    }

    /**
     * 通过getAssets来获取资源
     * @param context
     * @param fileName assets文件夹中的文件名
     * @return XmlPullParser
     */
    public static XmlPullParser getAssetsXmlPullParser(Context context,String fileName){
        try {
//            String xmlData=getAssetsData(context, fileName);//不这么用，因为转成string再转成inputstream耗费资源
            InputStream inputStream=context.getAssets().open(fileName);
            if(inputStream!=null){
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser=factory.newPullParser();
                xmlPullParser.setInput(inputStream,"UTF-8");
                return xmlPullParser;
            }

        } catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public static XmlPullParser getXmlPullParser(String xmlData){
        try {
            if(xmlData!=null){
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser=factory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlData));
                return xmlPullParser;
            }

        } catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public static String getFileInput(Context context,String fileName){
        if(context==null)
            return ERROR_RESPONSE;
        try {
            FileInputStream fis=context.openFileInput(fileName);
            FileChannel fileChannel=fis.getChannel();
            MappedByteBuffer mbbuf=fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            //使用gbk编码创建解码器
            Charset charset=Charset.forName("UTF-8");
            //创建解码器
            CharsetDecoder decoder=charset.newDecoder();

            //使用解码器将byteBuffer转成charBuffer
            CharBuffer charBuffer=decoder.decode(mbbuf);
            return charBuffer.toString();

        } catch (Exception e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return ERROR_RESPONSE;
    }

    public static boolean getFileOutput(Context context,String writeMsg,String fileName){
        if(context==null)
            return false;
        try {
            FileOutputStream fileOutputStream=context.openFileOutput(fileName, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(writeMsg);
            writer.close();
            return true;
        }catch (Exception e){
            LogUtils.e(TAG, Log.getStackTraceString(e));
        }
        return false;
    }

    /**
     * 通过得到arrays.xml中arrayResure的子项
     * 返回对应的资源数组
     * @param context
     * @param arrayResure
     * @return
     */
    public static int[] getResourseArray(Context context,int arrayResure){
        TypedArray ar = context.getResources().obtainTypedArray(arrayResure);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);

        ar.recycle();

        return resIds;
    }

    public static void saveObject(String path,Object saveObject){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File f = new File(path);
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(saveObject);
        } catch (Exception e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));

        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                LogUtils.e(TAG, Log.getStackTraceString(e));

            }
        }
    }

    /**
     * 通过path路径，读取对应的Object流
     * @param path
     * @return
     */
    public static Object restoreObject(final String path){
        if(!isExistFile(path)){
            return null;
        }
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        try{
            fis=new FileInputStream(path);
            ois=new ObjectInputStream(fis);
            return ois.readObject();
        }catch (Exception e){
            LogUtils.e(TAG,Log.getStackTraceString(e));
        }finally {
            try{
                if(ois!=null){
                    ois.close();
                }
                if (fis!=null){
                    fis.close();
                }
            }catch (Exception e1){
                LogUtils.e(TAG,Log.getStackTraceString(e1));

            }
        }
        return null;
    }

}
