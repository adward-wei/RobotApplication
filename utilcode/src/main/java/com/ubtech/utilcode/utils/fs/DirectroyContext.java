package com.ubtech.utilcode.utils.fs;

import java.util.Collection;
/**
 * 目录上下文类，主要提供上层进行具体目录管理集合的定义的扩展
 * 
 * @author devilxie
 * @version 1.0
 */
public abstract class DirectroyContext
{
	public static final int APP_ROOT_DIR = 1;
	protected Directory baseDirectory;
	
	public DirectroyContext(String root) {
		initContext(root);
	}
	
	protected DirectroyContext() {
		
	}
	
	public void initContext(String root)
	{
		Directory dir  = new Directory(root, null);
		this.baseDirectory = dir;
		dir.setType(APP_ROOT_DIR);
		
		Collection<Directory> children = initDirectories();
		if (children != null && children.size()  >0)
			dir.addChildren(children);
	}
	
	public Directory getBaseDirectory() {
		return baseDirectory;
	}
	
	protected  abstract Collection<Directory> initDirectories();	
	
}
