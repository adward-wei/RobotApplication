package com.ubtech.utilcode.utils;

import org.junit.Test;

import static com.ubtech.utilcode.utils.StringUtils.*;
import static com.google.common.truth.Truth.assertThat;

/**
 *
 *     @author: logic.peng
 *     @email  : pdlogic1987@gmail.com
 *     @time  : 2016/8/16
 *     desc  : StringUtils单元测试
 *
 */
public class StringUtilsTest {

    @Test
    public void testIsEmpty() throws Exception {
        assertThat(isEmpty("")).isTrue();
        assertThat(isEmpty(null)).isTrue();
//        assertThat(isEmpty(" ")).isFalse();
    }

    @Test
    public void testIsSpace() throws Exception {
        assertThat(isSpace("")).isTrue();
        assertThat(isSpace(null)).isTrue();
        assertThat(isSpace(" ")).isTrue();
        assertThat(isSpace("　")).isFalse();
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(StringUtils.equals("logic.peng", "logic.peng")).isEqualTo(true);
        assertThat(StringUtils.equals("logic.peng", "logic")).isEqualTo(false);
    }

    @Test
    public void testEqualsIgnoreCase() throws Exception {
        assertThat(equalsIgnoreCase("logic.peng", "logic.peng")).isEqualTo(true);
        assertThat(equalsIgnoreCase("logic.peng", "logic.peng")).isEqualTo(true);
        assertThat(equalsIgnoreCase("logic.peng", "blank")).isEqualTo(false);
    }

    @Test
    public void testNull2Length0() throws Exception {
        assertThat(null2Length0(null)).isEqualTo("");
    }

    @Test
    public void testLength() throws Exception {
        assertThat(length(null)).isEqualTo(0);
        assertThat(length("")).isEqualTo(0);
        assertThat(length("logic.")).isEqualTo(6);
    }

    @Test
    public void testUpperFirstLetter() throws Exception {
        assertThat(upperFirstLetter("logic.peng")).isEqualTo("Logic.peng");
        assertThat(upperFirstLetter("Logic.peng")).isEqualTo("Logic.peng");
        assertThat(upperFirstLetter("1logic.peng")).isEqualTo("1logic.peng");
    }

    @Test
    public void testLowerFirstLetter() throws Exception {
        assertThat(lowerFirstLetter("logic.peng")).isEqualTo("logic.peng");
        assertThat(lowerFirstLetter("logic.peng")).isEqualTo("logic.peng");
        assertThat(lowerFirstLetter("1logic.peng")).isEqualTo("1logic.peng");
    }

    @Test
    public void testReverse() throws Exception {
        assertThat(reverse("blankj")).isEqualTo("jknalb");
        assertThat(reverse("blank")).isEqualTo("knalb");
        assertThat(reverse("测试中文")).isEqualTo("文中试测");
        assertThat(reverse(null)).isNull();
    }

    @Test
    public void testToDBC() throws Exception {
        assertThat(toDBC("　，．＆")).isEqualTo(" ,.&");
    }

    @Test
    public void testToSBC() throws Exception {
        assertThat(toSBC(" ,.&")).isEqualTo("　，．＆");
    }
}