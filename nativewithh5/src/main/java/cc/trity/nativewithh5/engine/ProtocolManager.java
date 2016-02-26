package cc.trity.nativewithh5.engine;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import cc.trity.library.utils.LogUtils;
import cc.trity.nativewithh5.R;

/**
 * 通过findKey取到对应子节点的值
 * Created by TryIT on 2016/2/25.
 */
public class ProtocolManager {
    private static final String TAG="ProtocolManager";
    public static ProtocolData findProtocol(String findKey, final Activity activity) {
        ProtocolData findProtocol = null;

        final XmlResourceParser xmlParser = activity.getApplication()
                .getResources().getXml(R.xml.protocol);

        int eventCode;
        try {
            eventCode = xmlParser.getEventType();
            while (eventCode != XmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Node".equals(xmlParser.getName())) {
                            final String key = xmlParser.getAttributeValue(null,
                                    "Key");
                            if (key.equals(findKey)) {
                                ProtocolData protocol = new ProtocolData();
                                protocol.setKey(key);
                                protocol.setTarget(xmlParser.getAttributeValue(
                                        null, "Target"));

                                findProtocol = protocol;
                                break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventCode = xmlParser.next();
            }
        } catch (final XmlPullParserException e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));

        } catch (final IOException e) {
            LogUtils.e(TAG, Log.getStackTraceString(e));

        } finally {
            xmlParser.close();
        }

        return findProtocol;
    }
}
