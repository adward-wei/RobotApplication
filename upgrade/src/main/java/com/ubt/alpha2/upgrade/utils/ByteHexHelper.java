package com.ubt.alpha2.upgrade.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [十六进制据类型转换工具]
 * 
 * @author zengdengyi
 * @version 1.0
 * @date 2014-5-29
 * 
 **/

public class ByteHexHelper {
	private static boolean D = false;

	/**
	 * 将byte数组转换成16进制字符串 Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
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

	/**
	 * 将byte转换成16进制字符串
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param src
	 *            byte data
	 * @return hex string
	 */
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

	/**
	 * 将byte转换成10进制数
	 * 
	 * @param src
	 *            byte data
	 * @return int
	 */
	public static int byteToInt(byte src) {
		return src & 0xFF;
	}

	/**
	 * 十进制转为1个字节的byte[]
	 * 
	 * @param id
	 * @return
	 */
	public static byte[] intToHexBytes(int id) {
		String hexString = Integer.toHexString(id);
		int len = hexString.length();
		while (len < 2) {
			hexString = "0" + hexString;
			len = hexString.length();
		}
		return ByteHexHelper.hexStringToBytes(hexString);
	}

	/**
	 * 十进制转为2个字节的byte[]
	 * 
	 * @param id
	 * @return
	 */
	public static byte[] intToTwoHexBytes(int id) {
		String hexString = Integer.toHexString(id);
		int len = hexString.length();
		while (len < 4) {
			hexString = "0" + hexString;
			len = hexString.length();
		}
		return ByteHexHelper.hexStringToBytes(hexString);
	}

	/**
	 * 十进制转为4个字节的byte[]
	 * 
	 * @param id
	 * @return
	 */
	public static byte[] intToFourHexBytes(int id) {
		String hexString = Integer.toHexString(id);
		int len = hexString.length();
		while (len < 8) {
			hexString = "0" + hexString;
			len = hexString.length();
		}
		return ByteHexHelper.hexStringToBytes(hexString);
	}

	/**
	 * 十进制转为4个字节的byte[] 在后面补零
	 * 
	 * @param id
	 * @return
	 */
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

	/**
	 * 十进制转为1个字节的byte
	 * 
	 * @param id
	 * @return
	 */
	public static byte intToHexByte(int id) {
		String hexString = Integer.toHexString(id);
		int len = hexString.length();
		while (len < 2) {
			hexString = "0" + hexString;
			len = hexString.length();
		}
		return ByteHexHelper.hexStringToByte(hexString);
	}

	/**
	 * 将16进制字符串转换为byte数组 这方法又问题有待验证 接收的时候用这个
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
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

	/**
	 * 将16进制字符串转换为byte
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
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

	/**
	 * 修改后的 将16进制字符串转换为byte数组 发送的时候用这个
	 * 
	 * @author zengdengyi
	 * @param str
	 * @return
	 */

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

	/**
	 * 转换char类型到byte类型
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 将16进制字符串中每两位之间进行异或校验
	 * 
	 * @param hex
	 * @return
	 */
	public static String XOR(String hex) {
		byte bytes = (byte) (0x00);
		if (hex.length() > 0) {
			for (int i = 0; i < hex.length() / 2; i++) {
				bytes = (byte) (bytes ^ (ByteHexHelper.hexStringToByte(hex
						.substring(2 * i, 2 * i + 2))));
			}
		}
		byte[] bbb = { bytes };
		return ByteHexHelper.bytesToHexString(bbb);
	}

	/**
	 * 获取到当前的年 月 日 时 分 秒 星期
	 * 
	 * @return
	 */
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
		String week = decimalFormat
				.format(calendar.get(Calendar.DAY_OF_WEEK) - 1);
		stringBuffer.append(year.substring(2, year.length())).append(month)
				.append(day).append(hour).append(minute).append(second)
				.append(week);
		System.out.println(stringBuffer.toString());
		return stringBuffer.toString();
	}

	/**
	 * 生成0到99随机数,长度为2位
	 * 
	 * @return
	 */
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

	/**
	 * 根据字符串得到十六进制包长度
	 * 
	 * @param str
	 * @return
	 */
	public static String packLength(String str) {
		String hexLength = Integer.toHexString(str.length() / 2);// 十进制转换为十六进制字符串
		int len = hexLength.length();
		while (len < 4) {
			hexLength = "0" + hexLength;
			len = hexLength.length();
		}
		return hexLength;
	}

	/**
	 * 根据长度值十六进制包长度
	 * 
	 * @param str
	 * @return
	 */
	public static String checkedSite(int site) {
		String hexLength = Integer.toHexString(site);// 十进制转换为十六进制字符串
		int len = hexLength.length();
		while (len < 2) {
			hexLength = "0" + hexLength;
			len = hexLength.length();
		}
		return hexLength;
	}

	/**
	 * 根据长度值十六进制包长度
	 * 
	 * @param str
	 * @return
	 */
	public static String packLength(int dataLen) {
		String hexLength = Integer.toHexString(dataLen);// 十进制转换为十六进制字符串
		int len = hexLength.length();
		while (len < 4) {
			hexLength = "0" + hexLength;
			len = hexLength.length();
		}
		return hexLength;
	}

	/**
	 * 十进制包长度 十六进制字符串转换为十进制
	 * 
	 * @param str
	 * @return
	 */
	public static int intPackLength(String str) {
		int intLength = Integer.valueOf(str, 16);// 十六进制字符串转换为十进制
		return intLength;
	}

	/**
	 * 十进制包长度
	 * 将byte[]通过bytesToHexString方法转换成16进制string，然后再通过Integer.valueOf(str,16
	 * )将十六进制字符串转换为十进制
	 * 
	 * @param str
	 * @return intLength int
	 */
	public static int intPackLength(byte[] str) {
		String byteStr = bytesToHexString(str);
		int intLength = Integer.valueOf(byteStr, 16);// 十六进制字符串转换为十进制
		return intLength;
	}

	/**
	 * 包校验
	 * 
	 * @param target
	 * @param source
	 * @param packLengths
	 * @param counter
	 * @param commandWord
	 * @param dataArea
	 * @return
	 */
	public static String packVerify(String target, String source,
			String packLengths, String counter, String commandWord,
			String dataArea) {
		String verify = ByteHexHelper.XOR(target + source + packLengths
				+ counter + commandWord + dataArea);
		return verify;
	}

	/**
	 * 把String转换为DPUstring
	 * 
	 * @param str
	 * @return
	 */
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

	/**
	 * 二进制字符串转16进制字符串
	 * 
	 * @param bString
	 * @return
	 */
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

	/**
	 * 16进制字符串转二进制字符串
	 * 
	 * @param bString
	 * @return
	 */
	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * android 去除字符串中的前后空格、回车、换行符、制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest.trim();
	}

	/**
	 * @author 将DPU_String数组类 转换为 String数组类
	 * */
	public static ArrayList<String> toStringArray(byte[] data) {
		if (data != null) {
			int total_bytes = data.length;
			if (total_bytes >= 3) {// 最少一个DPU_String
				int walkthrough = 0;
				ArrayList<String> result_strings = new ArrayList<String>();
				while (walkthrough < (total_bytes - 1)) {
					int temp_len = data[walkthrough] << 8
							| data[walkthrough + 1];
					byte[] str_bytes = new byte[temp_len - 1];
					System.arraycopy(data, walkthrough + 2, str_bytes, 0,
							temp_len - 1);
					result_strings.add(new String(str_bytes));
					walkthrough += temp_len + 2;// 忽略2个头字节
				}
				return result_strings;
			}
		}
		return null;
	}

	/**
	 * @author
	 * @see 字节数组操作 System.arraycopy() 将data数组的数据附加到src数组之后
	 * */
	public static byte[] appendByteArray(byte[] src, byte[] data) {
		if (src.length > 0 && data.length > 0) {
			byte[] ret = new byte[src.length + data.length];
			System.arraycopy(src, 0, ret, 0, src.length);// copy source
			System.arraycopy(data, 0, ret, src.length, data.length);// copy data
			return ret;
		} else
			throw new IllegalArgumentException("字节数组参数错误");
	}

	// 单个文件的校验方法
	public static String calculateSingleFileMD5sum(File file) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(file);
		int readLen = 0;
		byte[] buff = new byte[256];
		while ((readLen = fis.read(buff)) != -1) {
			md5.update(buff, 0, readLen);
		}
		fis.close();// 务必关闭
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
