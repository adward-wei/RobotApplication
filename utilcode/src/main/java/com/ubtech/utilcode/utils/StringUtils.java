package com.ubtech.utilcode.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *     @author: logic.peng
 *     @email  : pdlogic1987@gmail.com
 *     @time  : 2016/8/16
 *     @desc  : 字符串相关工具类
 *
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }



    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 字符串转成int
     * @param s
     * @param defaultValue
     * @return
     */
    public static int stringToInt(String s,int defaultValue) {
        if (TextUtils.isEmpty(s)){
            return defaultValue;
        }
        Integer integer = defaultValue;
        try {
            integer = Integer.valueOf(s);
        } catch (Exception e){
            e.printStackTrace();
        }
        return integer.intValue();
    }

    /**
     *
     * 字符解析器
     */
    public interface CharSpeller
    {
        String spell(char c);
    }

    private final static char[]	HEX_DIGITS	= { '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static CharSpeller		speller		= null;

    public static void registerSpeller(CharSpeller cs)
    {
        speller = cs;
    }

    /**
     * 将指定字节转换成16进制字符
     *
     * @param b
     *            待转换字节
     * @return 返回转换后的字符串
     */
    public static String byteToHexDigits(byte b)
    {
        int n = b;
        if (n < 0)
            n += 256;

        int d1 = n / 16;
        int d2 = n % 16;

        return "" + HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 将指定字节数组转换成16进制字符串
     *
     * @param bytes
     *            待转换的字节数组
     * @return 返回转换后的字符串
     */
    public static String bytesToHexes(byte[] bytes)
    {
        if (bytes == null || bytes.length == 0)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            sb.append(byteToHexDigits(bytes[i]));
        }
        return sb.toString();
    }

    /**
     * 十六进制字符转换为整数
     *
     * @param hex
     *            小写十六进制字符
     * @return 返回整数
     */
    public static int hexToInteger(char hex) {
        if (hex >= HEX_DIGITS[16])
            return hex - HEX_DIGITS[16] + 10;
        else if (hex >= HEX_DIGITS[10])
            return hex - HEX_DIGITS[10] + 10;
        else
            return hex - HEX_DIGITS[0];
    }

    /**
     * 十六进制字符串转换为字节数组
     *
     * @param hexes
     *            十六进制字符串
     * @return 返回字节数组
     */
    public static byte[] hexesToBytes(String hexes)
    {
        if (hexes == null || hexes.length() == 0)
            return null;

        int slen = hexes.length();
        int len = (slen + 1)/ 2;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++)
        {
            char c = hexes.charAt(2 * i);
            int val = hexToInteger(c);
            val *= 16;
            if (2 * i + 1 < slen) {
                c = hexes.charAt(2 * i + 1);
                val += hexToInteger(c);
            }

            bytes[i] = (byte)(val & 0xff);

        }
        return bytes;
    }

    /**
     * 比较两个字符串大小，考虑汉字拼音顺序, 忽略大小写
     *
     * @param s1
     *            字符串1
     * @param s2
     *            字符串2
     * @return 返回比较结果。0： s1 = s2， >0： s1 > s2, <0: s1 < s2
     */
    @SuppressLint("DefaultLocale")
    public static int compareToIgnoreCase(String s1, String s2)
    {
        // 两者为空，相同
        if (s1 == null && s2 == null)
        {
            return 0;
        }
        // 某项为空，则以它为小
        if (s1 == null)
        {
            return -1;
        }

        if (s2 == null)
        {
            return 1;
        }

        if (s1.equals(s2))
        {
            return 0;
        }

        String s3 = s1.toLowerCase();
        String s4 = s2.toLowerCase();

        return compareToUnicode(s3, s4);
    }

    /**
     * 比较两个字符串大小，考虑汉字拼音顺序
     *
     * @param s1
     *            字符串1
     * @param s2
     *            字符串2
     * @return 返回比较结果。0： s1 = s2， >0： s1 > s2, <0: s1 < s2
     */
    public static int compareTo(String s1, String s2)
    {

        // 两者为空，相同
        if (s1 == null && s2 == null)
        {
            return 0;
        }
        // 某项为空，则以它为小
        if (s1 == null)
        {
            return -1;
        }

        if (s2 == null)
        {
            return 1;
        }

        if (s1.equals(s2))
        {
            return 0;
        }

        if (s1.length() == 0)
        {
            return -1;
        }

        if (s2.length() == 0)
        {
            return 1;
        }

        return compareToUnicode(s1, s2);
    }

    public static boolean isLetter(int ch)
    {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
    }

    public static boolean isDigit(int ch)
    {
        return ch >= '0' && ch <= '9';
    }

    static final int	UPPER_LOWER_SPAN	= 'A' - 'a';
    static final int	LOWER_UPPER_SPAN	= -UPPER_LOWER_SPAN;

    private static int compareToGBK(String s1, String s2)
    {
        int ret = 0;
        try
        {
            byte[] bytes1 = s1.getBytes("gbk");
            byte[] bytes2 = s2.getBytes("gbk");

            int len = Math.min(bytes1.length, bytes2.length);
            for (int i = 0; i < len; i++)
            {

                if (bytes1[i] > 0 && bytes2[i] > 0)
                {
                    ret = Character.toLowerCase(bytes1[i])
                            - Character.toLowerCase(bytes2[i]);
                    if (ret == 0)
                        ret = bytes1[i] - bytes2[i];
                }
                else
                {
                    int b1 = (bytes1[i] + 256) % 256;
                    int b2 = (bytes2[i] + 256) % 256;
                    ret = b1 - b2;
                }

                if (ret != 0)
                {
                    break;
                }

            }
            if (ret == 0)
            {
                ret = bytes1.length - bytes2.length;
            }

        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return ret;
    }

    private static int compareToUnicode(String s1, String s2)
    {

        if (speller == null)
        {
            return compareToGBK(s1, s2);
        }

        int ret = 0;
        int len = Math.min(s1.length(), s2.length());
        for (int i = 0; i < len; i++)
        {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);

            ret = compareTo(c1, c2);
            if (ret != 0)
                break;
        }

        if (ret == 0)
        {
            ret = s1.length() - s2.length();
        }

        return ret;
    }

    private static int compareTo(char c1, char c2)
    {
        int ret = 0;
        // 字母比较，直接比较ASCII
        if (isLetter(c1) && isLetter(c2))
        {
            ret = Character.toLowerCase(c1) - Character.toLowerCase(c2);
            if (ret == 0)
                ret = c1 - c2;

            return ret;
        }

        String s1 = null;
        String s2 = null;

        if (isLetter(c1))
        {
            s2 = speller.spell(c2);
            char cc2 = s2.charAt(0);
            if (isLetter(cc2))
            {
                ret = Character.toLowerCase(c1) - Character.toLowerCase(cc2);
                if (ret == 0)
                {
                    ret = 1;
                }

                return ret;
            }
            else
                return -1;
        }

        else if (isLetter(c2))
        {
            s1 = speller.spell(c1);
            char cc1 = s1.charAt(0);
            if (isLetter(cc1))
            {
                ret = Character.toLowerCase(cc1) - Character.toLowerCase(c2);
                if (ret == 0)
                {
                    ret = -1;
                }

                return ret;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            s1 = speller.spell(c1);
            s2 = speller.spell(c2);
        }

        int len = Math.min(s1.length(), s2.length());

        for (int i = 0; i < len; i++)
        {
            char cc1 = s1.charAt(i);
            char cc2 = s2.charAt(i);

            if (isLetter(cc1) && isLetter(cc2))
            {
                ret = Character.toLowerCase(cc1) - Character.toLowerCase(cc2);
                if (ret == 0)
                {
                    ret = cc1 - cc2;
                }
            }

            else if (isLetter(cc1))
            {
                ret = -1;
            }

            else if (isLetter(cc2))
            {
                ret = 1;
            }

            else
            {
                ret = cc1 - cc2;
            }

            if (ret != 0)
            {
                break;
            }
        }

        if (ret == 0)
        {
            ret = s1.length() - s2.length();
        }

        return ret;
    }

    @SuppressWarnings("unused")
    private static int compareToBigInteger(String s1, String s2)
    {
        int ret = 0;
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();

        int index1 = 0, index2 = 0;
        while (index1 < c1.length && c1[index1] == '0')
            index1++;
        while (index2 < c2.length && c2[index2] == '0')
            index2++;

        if (c1.length - index1 != c2.length - index2)
        {
            ret = (c1.length - index1) - (c2.length - index2);
        }
        else
        {
            ret = c1[index1] - c2[index2];
        }

        return ret;
    }

    /**
     * 将作为文件名的字符串的特殊字符"\*?:$/'",`^<>+"替换成"_"，以便文件顺利创建成功
     *
     * @param path
     *            原待创建的文件名
     * @return 返回处理后的文件名
     */
    public static String filterForFile(String path)
    {
        if (path == null || path.length() == 0)
        {
            return "";
        }
        String need = path.replaceAll(
                "\\\\|\\*|\\?|\\:|\\$|\\/|'|\"|,|`|\\^|<|>|\\+", "_");
        return need;
    }





    /**
     * 处理空字符串
     *
     * @param str
     * @return String
     */
    public static String doEmpty(String str) {
        return doEmpty(str, "");
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String doEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null")
                || str.trim().equals("") || str.trim().equals("－请选择－")) {
            str = defaultValue;
        } else if (str.startsWith("null")) {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * 请选择
     */
    final static String PLEASE_SELECT = "请选择...";

    public static boolean notEmpty(Object o) {
        return o != null && !"".equals(o.toString().trim())
                && !"null".equalsIgnoreCase(o.toString().trim())
                && !"undefined".equalsIgnoreCase(o.toString().trim())
                && !PLEASE_SELECT.equals(o.toString().trim());
    }

    public static boolean empty(Object o) {
        return o == null || "".equals(o.toString().trim())
                || "null".equalsIgnoreCase(o.toString().trim())
                || "undefined".equalsIgnoreCase(o.toString().trim())
                || PLEASE_SELECT.equals(o.toString().trim());
    }

    public static boolean num(Object o) {
        int n = 0;
        try {
            n = Integer.parseInt(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean decimal(Object o) {
        double n = 0;
        try {
            n = Double.parseDouble(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (n > 0.0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 给JID返回用户名
     *
     * @param Jid
     * @return
     */
    public static String getUserNameByJid(String Jid) {
        if (empty(Jid)) {
            return null;
        }
        if (!Jid.contains("@")) {
            return Jid;
        }
        return Jid.split("@")[0];
    }

    /**
     * 给用户名返回JID
     *
     * @param jidFor   域名//如ahic.com.cn
     * @param userName
     * @return
     */
    public static String getJidByName(String userName, String jidFor) {
        if (empty(jidFor) || empty(jidFor)) {
            return null;
        }
        return userName + "@" + jidFor;
    }

    /**
     * 给用户名返回JID,使用默认域名ahic.com.cn
     *
     * @param userName
     * @return
     */
    public static String getJidByName(String userName) {
        String jidFor = "ahic.com.cn";
        return getJidByName(userName, jidFor);
    }

    /**
     * 根据给定的时间字符串，返回月 日 时 分 秒
     *
     * @param allDate like "yyyy-MM-dd hh:mm:ss SSS"
     * @return
     */
    public static String getMonthTomTime(String allDate) {
        return allDate.substring(5, 19);
    }

    /**
     * 根据给定的时间字符串，返回月 日 时 分 月到分钟
     *
     * @param allDate like "yyyy-MM-dd hh:mm:ss SSS"
     * @return
     */
    public static String getMonthTime(String allDate) {
        return allDate.substring(5, 16);
    }


    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     * is null or its length is 0 or it is made by space
     * <p>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }


    public static boolean isNull(Object str) {
        return (str == null || str.toString().length() == 0 || "null"
                .equals(str.toString()));
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }


    /**
     * capitalize first letter
     * <p>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length())
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * <p>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * <p>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern
                .compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     * <p>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return isEmpty(source) ? source : source
                .replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * <p>
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * <p>
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    public static String nullStringToDefault(String str) {

        return (str == null || isEquals("null", str)) ? "" : str;
    }


    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     *
     * @param cardNum 待检验的原始卡号
     * @return 返回是否包含
     */
    public static boolean isContainsEnglish(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    public static boolean isContainsNumeric(String str) {

        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }


    public static boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();
    }

    public static boolean isPun(String str) {

        Pattern pattern =
                Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");

        return pattern.matcher(str).matches();
    }

    public static boolean isDigitOrChinese(String str) {
        String regex1 = "^[0-9\u4e00-\u9fa5,.?!，。？！]+$";
        return str.matches(regex1);
    }

    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z,.?!，。？！]+$";
        return str.matches(regex);
    }
}