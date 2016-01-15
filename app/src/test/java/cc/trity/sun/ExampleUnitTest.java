package cc.trity.sun;

import org.junit.Test;

import cc.trity.sun.model.Global;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void test_string_format() throws Exception{
        String address= Global.URL_PRIVENIC;
        String formatStr=String.format(address,null);
        assertEquals(address,formatStr);
    }
}