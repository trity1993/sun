package cc.trity.sun.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间的出列类
 * Created by TryIT on 2016/1/19.
 */
public class TimeUtils {
    /**
     *
     * 注意 SimpleDateFormat 是线程不安全的
     * @param Match 匹配的格式输出
     * @return 输出当前时间格式化的数据
     */
    public static String getCurentTime(String Match){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(Match);
        Date date=new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 截取小时和分钟
     * @param date
     * @return
     */
    public static String getHM(String date){
        if(date==null){
            return "";
        }
        else if(date.length()<8){
            return "";
        }
        return date.substring(8, 12);
    }

    /**
     *
     * @param strHourMinuite 格式为 0600
     * @return
     */
    public static String getCurAppointHour(String strHourMinuite){
        StringBuilder stringBuilder=new StringBuilder(getCurentTime("yyyyMMdd"));
        stringBuilder.append(strHourMinuite);
        return stringBuilder.toString();
    }
}
