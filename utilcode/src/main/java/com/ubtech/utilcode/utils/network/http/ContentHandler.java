package com.ubtech.utilcode.utils.network.http;

/**
 * 内容处理接口，用于将网络返回结果数据进行处理
 * @author devilxie
 * @version 1.0
 */
public interface ContentHandler {

	/**
	 * 准备预处理，该方法只会调用一次
	 * @param curpos 预处理数据起始位置
	 * @param total 预处理数据总大小，包括当前位置
	 * @return 返回预处理结果
	 */
	boolean prepare(long curpos, long total);
	
	/**
	 * 数据阶段性处理，该方法将被反复调用
	 * @param datas 待处理数据
	 * @param offset 起始位置
	 * @param len 数据总长度
	 * @return 返回处理结果，如果出现处理错误，无法恢复的，将返回false，以此关闭后续处理流程
	 */
	boolean handle(byte[] datas, int offset, int len);
	
	/**
	 * 结束处理，该方法只会调用一次
	 * @param cause 结束原因，可参考{NetworkError}
	 * @param curpos 结束时数据的当前位置
	 */
	void complete(int cause, int curpos);
	
}
