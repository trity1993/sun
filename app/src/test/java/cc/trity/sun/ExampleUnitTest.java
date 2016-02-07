package cc.trity.sun;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cc.trity.sun.model.Global;
import cc.trity.sun.model.ReponseForcecastWeather;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Ignore
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Ignore
    @Test
    public void test_string_format() throws Exception{
//        String address= Global.URL_PRIVENIC;
//        String formatStr=String.format(address,null);
//        assertEquals(address,formatStr);
    }
    @Ignore
    @Test
    public void test_string_subString() throws Exception{
        String appId= Global.APP_ID;
        String subAppid=appId.substring(0, 6);
        assertEquals("02031c",subAppid);
    }

    @Test
    public void test_Json_changer() throws Exception{
        String strJson="{\"c\":{\"c1\":\"101280101\",\"c2\":\"guangzhou\",\"c3\":\"广州\",\"c4\":\"guangzhou\",\"c5\":\"广州\",\"c6\":\"guangdong\",\"c7\":\"广东\",\"c8\":\"china\",\"c9\":\"中国\",\"c10\":\"1\",\"c11\":\"020\",\"c12\":\"510000\",\"c13\":113.265000,\"c14\":23.108000,\"c15\":\"43\",\"c16\":\"AZ9200\",\"c17\":\"+8\"},\"f\":{\"f1\":[{\"fa\":\"\",\"fb\":\"07\",\"fc\":\"\",\"fd\":\"11\",\"fe\":\"\",\"ff\":\"0\",\"fg\":\"\",\"fh\":\"0\",\"fi\":\"07:10|18:04\"},{\"fa\":\"07\",\"fb\":\"07\",\"fc\":\"14\",\"fd\":\"11\",\"fe\":\"0\",\"ff\":\"0\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"07:09|18:05\"},{\"fa\":\"07\",\"fb\":\"08\",\"fc\":\"14\",\"fd\":\"11\",\"fe\":\"0\",\"ff\":\"0\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"07:09|18:06\"}],\"f0\":\"201601191800\"}}";
        ReponseForcecastWeather weather= GsonUtils.getClass(strJson,ReponseForcecastWeather.class);
        Assert.assertNotNull(weather);
    }

}