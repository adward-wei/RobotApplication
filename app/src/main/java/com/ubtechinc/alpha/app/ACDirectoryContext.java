/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.app;

import android.content.Context;
import android.os.Environment;

import com.ubtech.utilcode.utils.fs.Directory;
import com.ubtech.utilcode.utils.fs.DirectroyContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @date 2016/8/3
 * @author paul.zhang@ubtrobot.com
 * @Description
 * @modifier
 * @modify_time
 */

public class ACDirectoryContext extends DirectroyContext {

	Context mContext;

	public ACDirectoryContext(Context context, String appName) {
		this.mContext = context;
		initContext(appName);
	}

	@Override
	public void initContext(String root) {
		if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			File fileDir = mContext.getFilesDir();
			String rootPath = fileDir.getAbsolutePath() + File.separator + root;
			super.initContext(rootPath);
		} else {
			String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + root;
			super.initContext(rootPath);
		}
	}

	@Override
	protected Collection<Directory> initDirectories() {
		List<Directory> children = new ArrayList<Directory>();

		Directory dir = newDirectory(DirType.log);
		children.add(dir);
		dir = newDirectory(DirType.image);
		children.add(dir);
		dir = newDirectory(DirType.crash);
		children.add(dir);
		dir = newDirectory(DirType.app);
		children.add(dir);
		dir = newDirectory(DirType.cache);
		children.add(dir);
		return children;
	}

	private Directory newDirectory(DirType type) {
		Directory child = new Directory(type.toString(), null);
		child.setType(type.value());
		if (type.equals(DirType.cache)) {
			child.setForCache(true);
			child.setExpiredTime(1000 * 60 * 60 * 24L);
		}

		return child;
	}
}
