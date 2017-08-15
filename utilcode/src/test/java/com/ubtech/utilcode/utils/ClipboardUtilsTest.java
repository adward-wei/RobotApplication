package com.ubtech.utilcode.utils;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

/**
 *
 *     @author: logic.peng
 *     @email  : pdlogic1987@gmail.com
 *     @time  : 2016/9/26
 *     desc  : ClipboardUtils单元测试
 *
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ClipboardUtilsTest {

    @Before
    public void setUp() throws Exception {
        TestUtils.init();
    }

    @Test
    public void testText() throws Exception {
        ClipboardUtils.copyText("test");
        assertThat(ClipboardUtils.getText()).isEqualTo("test");
    }

    @Test
    public void testUri() throws Exception {
        ClipboardUtils.copyUri(Uri.parse("http://www.baidu.com"));
        assert  "http://www.baidu.com".equals(ClipboardUtils.getUri().toString());
    }

    //@Test
    public void testIntent() throws Exception {
        ClipboardUtils.copyIntent(IntentUtils.getShareTextIntent("test"));
        assert "test".equals(ClipboardUtils.getText());
    }
}