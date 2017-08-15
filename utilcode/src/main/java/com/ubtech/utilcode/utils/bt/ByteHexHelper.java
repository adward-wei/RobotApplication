package com.ubtech.utilcode.utils.bt;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByteHexHelper {
    private static boolean D = false;

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static String byteToHexString(byte src) {
        StringBuilder stringBuilder = new StringBuilder("");
        int v = src & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }

    public static int byteToInt(byte src) {
        return src & 0xFF;
    }

    public static byte[] intToHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return ByteHexHelper.hexStringToBytes(hexString);
    }

    public static byte[] intToTwoHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 4) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return ByteHexHelper.hexStringToBytes(hexString);
    }

    public static byte[] intToFourHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 8) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return ByteHexHelper.hexStringToBytes(hexString);
    }

    public static byte[] intToFourHexBytesTwo(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        if (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        while (len < 8) {
            hexString = hexString + "0";
            len = hexString.length();
        }
        return ByteHexHelper.hexStringToBytes(hexString);
    }

    public static byte intToHexByte(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return ByteHexHelper.hexStringToByte(hexString);
    }

    public static byte[] hexStringToBytes2(String hexString) {
        if (hexString == null || hexString.equals("")) {
            byte[] bytes = new byte[0];
            return bytes;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte hexStringToByte(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d[0];
    }

    public static byte[] hexStringToBytes(String str) {

        byte d[];
        int c, tmp;
        String str1 = str.replace(" ", "");
        System.out.println(str1);
        d = new byte[str1.length() / 2];
        for (int i = 0; i < str1.length();) {
            tmp = (int) str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 0x60)
                c = (tmp - 0x61 + 10) * 16;
            else if (tmp > 0x40)
                c = (tmp - 0x41 + 10) * 16;
            else
                c = (tmp - 0x30) * 16;
            i++;
            tmp = (int) str1.substring(i, i + 1).getBytes()[0];
            if (tmp > 0x60)
                c = c + (tmp - 0x61 + 10);
            else if (tmp > 0x40)
                c = c + (tmp - 0x41 + 10);
            else
                c = c + (tmp - 0x30);

            d[i / 2] = (byte) c;
            i++;

        }
        return d;

    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String XOR(String hex) {
        byte bytes = (byte) (0x00);
        if (hex.length() > 0) {
            for (int i = 0; i < hex.length() / 2; i++) {
                bytes = (byte) (bytes ^ (ByteHexHelper.hexStringToByte(hex.substring(2 * i, 2 * i + 2))));
            }
        }
        byte[] bbb = { bytes };
        return ByteHexHelper.bytesToHexString(bbb);
    }

    public static String currentData() {
        StringBuffer stringBuffer = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        String year = decimalFormat.format(calendar.get(Calendar.YEAR));
        String month = decimalFormat.format(calendar.get(Calendar.MONTH) + 1);
        String day = decimalFormat.format(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = decimalFormat.format(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = decimalFormat.format(calendar.get(Calendar.MINUTE));
        String second = decimalFormat.format(calendar.get(Calendar.SECOND));
        String week = decimalFormat.format(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        stringBuffer.append(year.substring(2, year.length())).append(month).append(day).append(hour).append(minute).append(second).append(week);
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }

    public static String RandomMethod() {
        int random = (int) (Math.random() * 100);
        String hexString = Integer.toHexString(random);
        int len = hexString.length();
        while (len < 2) {
            hexString = "0" + hexString;
            len = hexString.length();
        }
        return hexString;
    }

    public static String packLength(String str) {
        String hexLength = Integer.toHexString(str.length() / 2);
        int len = hexLength.length();
        while (len < 4) {
            hexLength = "0" + hexLength;
            len = hexLength.length();
        }
        return hexLength;
    }

    public static String checkedSite(int site) {
        String hexLength = Integer.toHexString(site);
        int len = hexLength.length();
        while (len < 2) {
            hexLength = "0" + hexLength;
            len = hexLength.length();
        }
        return hexLength;
    }

    public static String packLength(int dataLen) {
        String hexLength = Integer.toHexString(dataLen);
        int len = hexLength.length();
        while (len < 4) {
            hexLength = "0" + hexLength;
            len = hexLength.length();
        }
        return hexLength;
    }

    public static int intPackLength(String str) {
        int intLength = Integer.valueOf(str, 16);
        return intLength;
    }

    public static int intPackLength(byte[] str) {
        String byteStr = bytesToHexString(str);
        int intLength = Integer.valueOf(byteStr, 16);
        return intLength;
    }

    public static String packVerify(String target, String source, String packLengths, String counter, String commandWord, String dataArea) {
        String verify = ByteHexHelper.XOR(target + source + packLengths + counter + commandWord + dataArea);
        return verify;
    }

    public static String dpuString(String str) {
        String buffer = "";
        if (str != null && str.length() > 0) {
            byte[] src = (str + "\0").getBytes();
            String result = ByteHexHelper.bytesToHexString(src);
            String resultLength = ByteHexHelper.packLength(result);
            buffer = resultLength + result;
            System.out.println("resultLength==" + buffer);
        }
        return buffer;
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("")) {
            return "";
        }
        if (bString.length() % 8 != 0) {
            int addLen = 8 - bString.length() % 8;
            for (int i = 0; i < addLen; i++) {
                bString = bString + "0";
            }
            System.out.println("choiceItem = " + bString);
        }
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        System.out.println("tmp.toString() = " + tmp.toString());
        return tmp.toString();
    }

    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest.trim();
    }

    public static ArrayList<String> toStringArray(byte[] data) {
        if (data != null) {
            int total_bytes = data.length;
            if (total_bytes >= 3) {
                int walkthrough = 0;
                ArrayList<String> result_strings = new ArrayList<String>();
                while (walkthrough < (total_bytes - 1)) {
                    int temp_len = data[walkthrough] << 8 | data[walkthrough + 1];
                    byte[] str_bytes = new byte[temp_len - 1];
                    System.arraycopy(data, walkthrough + 2, str_bytes, 0, temp_len - 1);
                    result_strings.add(new String(str_bytes));
                    walkthrough += temp_len + 2;
                }
                return result_strings;
            }
        }
        return null;
    }

    public static byte[] appendByteArray(byte[] src, byte[] data) {
        if (src.length > 0 && data.length > 0) {
            byte[] ret = new byte[src.length + data.length];
            System.arraycopy(src, 0, ret, 0, src.length);// copy source
            System.arraycopy(data, 0, ret, src.length, data.length);// copy data
            return ret;
        } else
            throw new IllegalArgumentException("�ֽ������������");
    }

    public static String calculateSingleFileMD5sum(File file) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        int readLen = 0;
        byte[] buff = new byte[256];
        while ((readLen = fis.read(buff)) != -1) {
            md5.update(buff, 0, readLen);
        }
        fis.close();
        StringBuilder sb = new StringBuilder();
        byte[] data = md5.digest();
        for (byte b : data) {
            sb.append(new Formatter().format("%02x", b));
        }
        return sb.toString();
    }

    public static String parseAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (int i = 0; i < bs.length; i++)
            sb.append(toHex(bs[i]));
        return sb.toString();
    }

    public static String toHex(int n) {
        StringBuilder sb = new StringBuilder();
        if (n / 16 == 0) {
            return toHexUtil(n);
        } else {
            String t = toHex(n / 16);
            int nn = n % 16;
            sb.append(t).append(toHexUtil(nn));
        }
        return sb.toString();
    }

    // add by weizewei
    private static String toHexUtil(int n) {
        String rt = "";
        switch (n) {
        case 10:
            rt += "A";
            break;
        case 11:
            rt += "B";
            break;
        case 12:
            rt += "C";
            break;
        case 13:
            rt += "D";
            break;
        case 14:
            rt += "E";
            break;
        case 15:
            rt += "F";
            break;
        default:
            rt += n;
        }
        return rt;
    }

}
