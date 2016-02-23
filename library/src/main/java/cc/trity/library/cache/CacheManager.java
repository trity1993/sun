package cc.trity.library.cache;

import android.content.Context;

import java.io.File;

import cc.trity.library.utils.EncryptUtils;
import cc.trity.library.utils.FileUtils;

/**
 * 用于缓存数据的管理器
 * Created by TryIT on 2016/2/20.
 */
public class CacheManager {

    /**
     * 缓存文件路径
     */
    public final String APP_CACHE_PATH;

    /**
     * sdcard 最小空间，如果小于10M，不会再向sdcard里面写入任何数据
     */
    public static final long SDCARD_MIN_SPACE = 1024 * 1024 * 10;

    private static CacheManager cacheManager;
    private Context context;

    private CacheManager(Context context) {
        this.context = context;
        APP_CACHE_PATH = FileUtils.getDiskCacheDirPath(context) + File.separator + "weatherData";
    }

    public static CacheManager getInstance(Context context) {
        if (CacheManager.cacheManager == null) {
            synchronized (CacheManager.class) {
                CacheManager.cacheManager = new CacheManager(context);
            }
        }
        return CacheManager.cacheManager;
    }

    /**
     * 初始化缓存文件夹（进行创建）
     */
    public void initCacheDir() {
        // sdcard已经挂载并且空间不小于10M，可以写入文件;小于10M时，清除缓存
        if (FileUtils.getDiskCacheDirSize(context) < SDCARD_MIN_SPACE) {
            clearAllData();
        } else {
            final File dir = new File(APP_CACHE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    /**
     * 通过对key进行md5加密后取出对应的文件缓存
     *
     * @param key
     * @return
     */
    public String getFileCache(final String key) {
        String md5Key = EncryptUtils.getMd5(key);
        String keyPath = APP_CACHE_PATH + File.separator + md5Key;
        if (FileUtils.isExistFile(keyPath)) {
            final CacheItem item = getFormCache(keyPath);
            if (item != null) {
                return item.getData();
            }
        }
        return null;
    }

    /**
     * API data 缓存到文件
     *
     * @param key
     * @param data
     * @param expiredTime
     */
    public void putFileCache(final String key, final String data,
                             long expiredTime) {
        String md5Key = EncryptUtils.getMd5(key);

        final CacheItem item = new CacheItem(md5Key, data, expiredTime);
        putIntoCache(item);
    }

    public synchronized CacheItem getFormCache(final String path) {
        CacheItem cacheItem = null;
        Object findItem = FileUtils.restoreObject(path);
        if (findItem != null) {
            cacheItem = (CacheItem) findItem;
        }
        // 缓存不存在
        if (cacheItem == null)
            return null;
        // 缓存过期
        if (System.currentTimeMillis() > cacheItem.getTimeStamp()) {
            return null;
        }
        return cacheItem;
    }

    /**
     * 将CacheItem缓存到磁盘
     *
     * @param item
     * @return 是否缓存，True：缓存成功，False：不能缓存
     */

    public synchronized boolean putIntoCache(final CacheItem item) {
        if (FileUtils.getDiskCacheDirSize(context) > SDCARD_MIN_SPACE) {
            FileUtils.saveObject(APP_CACHE_PATH, item);
            return true;
        }
        return false;
    }

    /**
     * 清除缓存文件
     */
    public void clearAllData() {
        File file = new File(APP_CACHE_PATH);
        File[] files = file.listFiles();
        if (files != null) {
            for (final File file1 : files) {
                file1.delete();
            }
        }
    }

}
