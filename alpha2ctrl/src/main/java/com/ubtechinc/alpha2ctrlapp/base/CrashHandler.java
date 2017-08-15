package com.ubtechinc.alpha2ctrlapp.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 * @author way
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log tag */
	public final String TAG = "CrashHandler";
	private static CrashHandler instance;// CrashHandler实例
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");// 用于格式化日期,作为日志文件名的一部分
	/**
	 * 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能
	 * */
	public final boolean DEBUG = true;
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;

	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private Properties mDeviceCrashInfo = new Properties();
	private final String APP_NAME = "appName";
	private final String VERSION_NAME = "versionName";
	private final String VERSION_CODE = "versionCode";
	private final String STACK_TRACE = "STACK_TRACE";
	/** 错误报告文件的扩展名 */
	private final String CRASH_REPORTER_EXTENSION = ".cr";


	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {

	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if(instance==null)
			instance = new CrashHandler();
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				thread.sleep(3000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MobclickAgent.onKillProcess(mContext);
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(final Throwable ex) {
		if (ex == null)
			return false;
		mDeviceCrashInfo.put(APP_NAME, "com.ubtechinc.alpha2ctrlapp");
		final String msg = ex.getLocalizedMessage();
		new Thread() {
			public void run() {
				Looper.prepare();
				ToastUtils.showShortToast(  R.string.crash_hint);
				// 收集设备参数信息
				collectDeviceInfo(mContext);
				// 保存日志文件
				saveCrashInfo2File(ex);
				MobclickAgent.reportError(mContext,ex);

				Looper.loop();
			}
		}.start();


		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {


				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);

				mDeviceCrashInfo.put("crashTime",
						TimeUtils.getCurrentTimeInString());

			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get("").toString());
				Log.d(TAG, field.getName() + ":" + field.get(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private String saveCrashInfo2File(Throwable ex) {


		StringBuffer sb = new StringBuffer();
		for (Map.Entry<Object, Object> entry : mDeviceCrashInfo.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		mDeviceCrashInfo.put(STACK_TRACE, result);
		// 这里把刚才异常堆栈信息写入SD卡的Log日志里面

		String localFileUrl = writeLog(sb.toString());
		Logger.d("崩溃信息已经写入路径： " + localFileUrl);
		Logger.i(result);


		return null;
	}

	/**
	 *
	 * @param log
	 * @return 返回写入的文件路径 写入Log信息的方法，写入到SD卡里面
	 */
	private String writeLog(String log) {
		//保存文件
		long timetamp = System.currentTimeMillis();
		String time = format.format(new Date());
		String path = Environment.getExternalStorageDirectory().toString()+"/ubtech/alpha2/crash/";
		String fileName = path + "crash-" + time + "-" + timetamp + ".log";

		File file = new File(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			Log.d(TAG, "写入crash信息到SD卡里面=====");
			file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			// 写入相关Log到文件
			bw.write(log);
			bw.newLine();
			bw.close();
			fw.close();
			return fileName;
		} catch (IOException e) {
			Logger.d(TAG, "an error occured while writing file...", e);
			e.printStackTrace();
			return null;
		}



	}
}
