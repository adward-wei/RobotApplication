package com.ubtech.utilcode.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 *
 *     @author: logic.peng
 *     @email  : pdlogic1987@gmail.com
 *     @time  : 2016/8/23
 *     desc  : SDCard单元测试
 *
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SDCardUtilsTest {

    @Test
    public void testIsSDCardEnable() throws Exception {
        System.out.println(SDCardUtils.isSDCardEnable());
    }

    @Test
    public void testGetSDCardPath() throws Exception {
        System.out.println(SDCardUtils.getSDCardPath());
    }
}