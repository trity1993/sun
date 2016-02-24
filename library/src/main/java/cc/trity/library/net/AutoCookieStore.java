package cc.trity.library.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.trity.library.utils.LogUtils;

/**
 * 自定义cookiestore，自动进行保存和读取，更新等操作
 * Created by TryIT on 2016/2/23.
 */
public class AutoCookieStore implements CookieStore {
    private static final String TAG = "AutoCookieStore";

    /*
     * The memory storage of the cookies
     * cookie列表，每个url，对应一个list的cookie
     */
    private Map<URI, List<HttpCookie>> mapCookies = new HashMap<URI, List<HttpCookie>>();
    /*
     * The instance of the shared preferences
     */
    private final SharedPreferences spePreferences;

    public AutoCookieStore(Context ctxContext) {
        spePreferences = ctxContext.getSharedPreferences("CookiePrefsFile", Context.MODE_PRIVATE);
        Map<String, ?> prefsMap = spePreferences.getAll();

        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {

            for (String strCookie : (HashSet<String>) entry.getValue()) {

                if (!mapCookies.containsKey(entry.getKey())) {

                    List<HttpCookie> lstCookies = new ArrayList<>();
                    lstCookies.addAll(HttpCookie.parse(strCookie));

                    try {
                        mapCookies.put(new URI(entry.getKey()), lstCookies);
                    } catch (URISyntaxException e) {
                        LogUtils.e(TAG, Log.getStackTraceString(e));
                    }

                } else {

                    List<HttpCookie> lstCookies = mapCookies.get(entry.getKey());
                    lstCookies.addAll(HttpCookie.parse(strCookie));

                    try {
                        mapCookies.put(new URI(entry.getKey()), lstCookies);
                    } catch (URISyntaxException e) {
                        LogUtils.e(TAG, Log.getStackTraceString(e));
                    }

                }
                LogUtils.d(TAG,entry.getKey() + ": " + strCookie);
            }
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        LogUtils.d(TAG, "add " + cookie.toString());

        List<HttpCookie> cookies = mapCookies.get(uri);//尝试读取，没有则进行加入操作
        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();
            mapCookies.put(uri, cookies);
        }
        cookies.add(cookie);//加入到cookie的队列

        //写入到sharepreference中
        SharedPreferences.Editor ediWriter = spePreferences.edit();
        HashSet<String> setCookies = new HashSet<>();
        setCookies.add(cookie.toString());
        ediWriter.putStringSet(uri.toString(), spePreferences.getStringSet(uri.toString(),
                setCookies));
        ediWriter.commit();
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> lstCookies = mapCookies.get(uri);

        if (lstCookies == null)
            mapCookies.put(uri, new ArrayList<HttpCookie>());

        return mapCookies.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        Collection<List<HttpCookie>> values = mapCookies.values();

        List<HttpCookie> result = new ArrayList<>();
        for (List<HttpCookie> value : values) {
            result.addAll(value);
        }

        return result;
    }

    @Override
    public List<URI> getURIs() {
        Set<URI> keys = mapCookies.keySet();
        return new ArrayList<>(keys);
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        List<HttpCookie> lstCookies = mapCookies.get(uri);

        if (lstCookies == null)
            return false;

        return lstCookies.remove(cookie);
    }

    @Override
    public boolean removeAll() {
        mapCookies.clear();
        return true;
    }
}
