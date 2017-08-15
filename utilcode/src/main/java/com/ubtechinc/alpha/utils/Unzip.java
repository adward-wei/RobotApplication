package com.ubtechinc.alpha.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * 解压Zip文件工具类
 * 
 * @author zhangyongbo
 *
 */
public class Unzip {

	private static final int buffer = 2048;
	private UnzipListener listener;

	public Unzip(UnzipListener listener) {
		this.listener = listener;
	}

	public interface UnzipListener {
		public void onResult(int code, String msg);
	}

	/**
	 * 解压Zip文件
	 * 
	 * @param path
	 *            文件目录
	 */
	public void unZip(final String path, final String des) {
		new Thread() {
			public void run() {
				File f = new File(path);
				if (!f.exists()) {
					listener.onResult(-1, "file not exists");
					return;
				}
				int code = 1;
				int count = -1;
				String savepath = "";
				String msg = null;
				File file = null;
				InputStream is = null;
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;

				savepath = des + File.separator;// path.substring(0,
												// path.lastIndexOf("."))
				// + File.separator; // 保存解压文件目录
				new File(savepath).mkdir(); // 创建保存目录
				ZipFile zipFile = null;
				try {
					zipFile = new ZipFile(path, "gbk"); // 解决中文乱码问题
					Enumeration<?> entries = zipFile.getEntries();

					while (entries.hasMoreElements()) {
						byte buf[] = new byte[buffer];

						ZipEntry entry = (ZipEntry) entries.nextElement();

						String filename = entry.getName();
						boolean ismkdir = false;
						if (filename.lastIndexOf("/") != -1) { // 检查此文件是否带有文件夹
							ismkdir = true;
						}
						filename = savepath + filename;

						if (entry.isDirectory()) { // 如果是文件夹先创建
							file = new File(filename);
							file.mkdirs();
							continue;
						}
						file = new File(filename);
						if (!file.exists()) { // 如果是目录先创建
							if (ismkdir) {
								new File(filename.substring(0,
										filename.lastIndexOf("/"))).mkdirs(); // 目录先创建
							}
						}
						file.createNewFile(); // 创建文件

						is = zipFile.getInputStream(entry);
						fos = new FileOutputStream(file);
						bos = new BufferedOutputStream(fos, buffer);

						while ((count = is.read(buf)) > -1) {
							bos.write(buf, 0, count);
						}
						bos.flush();
						bos.close();
						fos.close();

						is.close();
					}

					zipFile.close();

				} catch (IOException ioe) {
					ioe.printStackTrace();
					msg = ioe.getMessage();
					code = 0;
				} finally {
					try {
						if (bos != null) {
							bos.close();
						}
						if (fos != null) {
							fos.close();
						}
						if (is != null) {
							is.close();
						}
						if (zipFile != null) {
							zipFile.close();
						}
						deleteFile(path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				listener.onResult(code, msg);
				
			}
		}.start();

	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public void deleteFile(String sPath) {
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

	public static void main(String[] args) {
		new Unzip(new UnzipListener() {

			@Override
			public void onResult(int code, String msg) {

			}


		}).unZip("讯飞舞蹈1.zip", "test");

	}
}