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
}
