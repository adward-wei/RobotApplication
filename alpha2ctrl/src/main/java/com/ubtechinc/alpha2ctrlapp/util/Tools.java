package com.ubtechinc.alpha2ctrlapp.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

	/**
	 * 判断是否含有汉字
	 *
	 * @return
	 */
	public static Boolean isContainChinese(String str) {

		Boolean bl = false;

		// 首先,使用Pattern解释要使用的正则表达式，其中^表是字符串的开始，$表示字符串的结尾。
		Pattern pt = Pattern.compile("^[\u4e00-\u9fa5]+$");
		// 然后使用Matcher来对比目标字符串与上面解释得结果
		Matcher mt = pt.matcher(str);
		// 如果能够匹配则返回true。实际上还有一种方法mt.find()，某些时候，可能不是比对单一的一个字符串，
		// 可能是一组，那如果只要求其中一个字符串符合要求就可以用find方法了.
		if (mt.find()) {
			bl = true;
		}
		return bl;
	}

	public static boolean isUserName(String str) {

		Pattern p = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{1,15}");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	public static Boolean isCorrectUserFormat(String str) {
		return true;
		// Boolean bl = false;
		// //首先,使用Pattern解释要使用的正则表达式，其中^表是字符串的开始，$表示字符串的结尾。
		// Pattern pt = Pattern.compile("^[a-zA-Z][0-9a-zA-Z_]+$");
		// //然后使用Matcher来对比目标字符串与上面解释得结果
		// Matcher mt = pt.matcher(str);
		// //如果能够匹配则返回true。实际上还有一种方法mt.find()，某些时候，可能不是比对单一的一个字符串，
		// //可能是一组，那如果只要求其中一个字符串符合要求就可以用find方法了.
		// if(mt.matches()){
		// bl = true;
		// }
		// return bl;
	}
	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	public static String getActionTime(long time) {
		String text = "";
		if (time < 60) {
			text = "00:" + time;
		}
		if (time > -60 && time < 600) {
			if (time % 60 < 10) {
				text = "0" + time / 60 + ":" + "0" + time % 60;
			} else {
				text = "0" + time / 60 + ":" + time % 60;
			}
		}
		if (time > 600 && time < 3600) {
			if (time % 60 < 10) {
				text = time / 60 + ":" + "0" + time % 60;
			} else {
				text = time / 60 + ":" + time % 60;
			}
		}
		return text;
	}








	/**
	 * 判断字符串是否为纯数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
	/**
	 * 判断字符串是位手机号
	 *
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		String telRegex = "[1][358]\\d{9}|17[0678]\\d{8}|14[57]\\d{8}";
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);

	}

	/**
	 * 判断邮箱是否合法
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean mailAddressVerify(String mailAddress) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(mailAddress);
		return m.matches();
	}




	/**
	 * @param date1
	 * @return 0 表示没有登录过，1表示登录有效，2表示登录失效
	 */
	public static int needLogin(String date1) {

		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		try

		{
			Date d1 = df.parse(getCurrentTime());
			if (TextUtils.isEmpty(date1)) {
				Logger.i("nxy: meiyou denglu ");
				return BusinessConstants.LOGIN_STATE_NO_LOGIN;//
			}
			Date d2 = df.parse(date1);
			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff - days * (1000 * 60 * 60 * 24))
					/ (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
					* (1000 * 60 * 60))
					/ (1000 * 60);
			Logger.i("nxy-> "+ days + "天" + hours + "小时" + minutes + "分");
			if (days < 7) {
				return BusinessConstants.LOGIN_STATE_LOGIN_EFFECTIVE;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return BusinessConstants.LOGIN_STATE_LOGIN_TIME_OUT;
	}



	public static Boolean isCorrectPswFormat(String str) {

		String str2 = "^[a-zA-Z0-9_]+$";
		Pattern p = Pattern.compile(str2);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 检查是否由汉字数字或者字母构成
	 *
	 * @param str
	 * @return
	 */
	public static Boolean isLetterOrDigitalChinese(String str) {
		String str2 = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
		Pattern p = Pattern.compile(str2);
		Matcher m = p.matcher(str);
		return m.matches();
	}


	/**
	 * 检查是否由数字或者字母构成
	 *
	 * @param str
	 * @return
	 */
	public static Boolean isLetterOrDigital(String str) {
		String str2 = "^[a-zA-Z0-9]+$";
		Pattern p = Pattern.compile(str2);
		Matcher m = p.matcher(str);
		return m.matches();
	}





	/**
	 * 给用户名返回JID
	 *
	 * @param jidFor   域名//如ahic.com.cn
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName, String jidFor) {

		return userName + jidFor;
	}

	/**
	 * 给用户名返回JID,使用默认域名ahic.com.cn
	 *
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName) {
		String jidFor = "@service.ubt.openfire";
		return getJidByName(userName, jidFor);
	}



	/**
	 * 转换char类型到byte类型
	 *
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}



	public static boolean isAppHasUpdate(Context context, String packageName, int versionCode) {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packages = packageManager.getInstalledPackages(0);

		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
			if (StringUtils.isEquals(packageInfo.packageName, packageName)) {
				if (versionCode > packageInfo.versionCode) {
					return true;
				}
			}


		}
		return false;
	}



	/**
	 * 判断openfire用户的状态
	 * strUrl : url格式 -http://47.88.12.227:9090/plugins/presence/status?jid=17@service.ubt.openfire&type=xml
	 * 返回值 : 0 - 用户不存在; 1 - 用户在线; 2 - 用户离线
	 * 说明   ：必须要求 openfire加载 presence 插件，同时设置任何人都可以访问
	 */
	public static short IsUserOnLine(String strUrl) {
		short shOnLineState = 0;    //-不存在-

		try {
			URL oUrl = new URL(strUrl);
			URLConnection oConn = oUrl.openConnection();
			if (oConn != null) {
				BufferedReader oIn = new BufferedReader(new InputStreamReader(oConn.getInputStream()));
				if (null != oIn) {
					String strFlag = oIn.readLine();
					oIn.close();

					if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
						shOnLineState = 2;
					}
					if (strFlag.indexOf("type=\"error\"") >= 0) {
						shOnLineState = 0;
					} else if (strFlag.indexOf("priority") >= 0 || strFlag.indexOf("id=\"") >= 0) {
						shOnLineState = 1;
					}
				}
			}
		} catch (Exception e) {
		}

		return shOnLineState;
	}

	public static void runAppWithoutRepeat(Context context, String packageName) throws PackageManager.NameNotFoundException {
		PackageInfo pi;
		pi = context.getPackageManager().getPackageInfo(packageName, 0);
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.setPackage(pi.packageName);
		PackageManager pManager = context.getPackageManager();
		List<ResolveInfo> apps = pManager.queryIntentActivities(
				resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			packageName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;
			Intent intent = new Intent(Intent.ACTION_MAIN);
			ComponentName cn = new ComponentName(packageName, className);
			intent.setComponent(cn);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

			context.startActivity(intent);
//				L.d(TAG, "run third app packageName = " + packageName);
		}

	}

	private static long lastClickTime;

	public synchronized static boolean isFastClick(long spacetime) {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < spacetime) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 判断当前系统是否中文
	 *
	 * @param context
	 * @return
	 */
	public static boolean isCh(Context context) {
		boolean flag = false;
		String currentSetLanguage = SPUtils.get().getString(Constants.APP_LAUNGUAGE);
		if (currentSetLanguage.equals(BusinessConstants.APP_LANGUAGE_EN) || !isZh(context)) {
			flag = false;
		} else if (currentSetLanguage.equals(BusinessConstants.APP_LANGUAGE_CN) || (TextUtils.isEmpty(currentSetLanguage) && isZh(context))) {
			flag = true;
		}
		return flag;

	}



	/**
	 * 是否中文
	 *
	 * @return
	 */
	public static boolean isZh(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	public static boolean isShowBottom(AlphaParam alphaParam, Context context) {
		if (Tools.isCh(context) && RobotManagerService.getInstance().isRobotOwner() && (null != alphaParam && alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE))) {
			return true;
		} else {
			return false;
		}
	}

	public void toast(int code, String message) {
		LoadingDialog.dissMiss();
		switch (code) {
			case 1:

				ToastUtils.showShortToast( message);
				break;
			case -1:
				ToastUtils.showShortToast( R.string.error_message_outLogin);
				break;
			case -2:
				ToastUtils.showShortToast( message);
				break;
			case 1001:
				ToastUtils.showShortToast( R.string.error_message_1001);
				break;
			case 1002:
				ToastUtils.showShortToast( R.string.error_message_1002);
				break;
			case 1003:
				ToastUtils.showShortToast( R.string.error_message_1003);
				break;
			case 1004:
				ToastUtils.showShortToast( R.string.error_message_1004);
				break;
			case 1005:
				ToastUtils.showShortToast( R.string.error_message_1005);
				break;
			case 1006:
				ToastUtils.showShortToast( R.string.error_message_1006);
				break;
			case 2001:
				ToastUtils.showShortToast( R.string.error_message_2001);
				break;
			case 2002:
				ToastUtils.showShortToast( R.string.error_message_2002);
				break;
			case 3001:
				ToastUtils.showShortToast( R.string.error_message_3001);
				break;
			case 4200:
				ToastUtils.showShortToast( R.string.error_message_4002);
				break;
			case 5001:
				ToastUtils.showShortToast( R.string.error_message_5001);
				break;
			case 5002:
				//  ToastUtils.showShortToast( R.string.error_message_5002);
				break;
			case 5003:
				ToastUtils.showShortToast( R.string.error_message_5003);
				break;
			case 5005:
				//ToastUtils.showShortToast( R.string.error_message_5005);
				break;
			case 5009:
				ToastUtils.showShortToast( R.string.error_message_5009);
				break;
			case 9999:
				ToastUtils.showShortToast( R.string.error_message_9999);
				break;
			case 10001:
				ToastUtils.showShortToast( R.string.error_message_server_unavailable);
				break;
			case 10002:
				ToastUtils.showShortToast( R.string.error_message_unrecoverable_error);
				break;
			case 1008:
				ToastUtils.showShortToast( R.string.error_message_1008);
				break;
			case 1009:
				ToastUtils.showShortToast( R.string.error_message_1009);
				break;
			default:
				break;
		}
	}
}
