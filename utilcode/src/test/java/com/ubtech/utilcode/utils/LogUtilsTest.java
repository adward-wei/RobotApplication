package com.ubtech.utilcode.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

/**
 *
 *     @author: logic.peng
 *     @email  : pdlogic1987@gmail.com
 *     @time  : 2016/10/9
 *     desc  : LogUtils单元测试
 *
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LogUtilsTest {

    @Before
    public void setUp() throws Exception {
        TestUtils.init();
        ShadowLog.stream = System.out;
        LogUtils.getBuilder("test").
                setLogSwitch(true).
                setLog2FileSwitch(false).
                setTag("Logic").
                create();
    }

    @Test
    public void v() throws Exception {
        LogUtils.v("Verbose");
    }

    @Test
    public void d() throws Exception {
        LogUtils.d("Debug");
    }

    @Test
    public void i() throws Exception {
        LogUtils.i("Info");
    }

    @Test
    public void w() throws Exception {
        LogUtils.w("Warn");
    }

    @Test
    public void e() throws Exception {
        LogUtils.e("Error");
    }
}