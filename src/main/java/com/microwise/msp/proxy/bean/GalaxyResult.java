package com.microwise.msp.proxy.bean;

import java.io.Serializable;

/**
 * webservice 出参对象
 * 
 * @author xubaoji
 * @date 2013-1-6
 * 
 * @check 2013-1-10 zhangpeng svn：1107
 */
public class GalaxyResult<T> implements Serializable {

	private static final long serialVersionUID = 659445371776395679L;

	/** 错误代码失败(服务端出现异常)标识常量 */
	public final static int ERRORCODE_FAIL = 0;

	/** 错误代码正常标识常量 */
	public final static int ERRORCODE_NORMAL = 1;

	/** 错误代码参数传递有误标识常量 **/
	public final static int ERRORCODE_PARAM = 2;

	/** 返回结果集只有一条的标识常量 */
	public final static int ONE_RESULTCOUNT = 1;

	/** 错误代码 */
	private int errorCode;

	/** 返回结果集条数 */
	private int resultCount;

	/** 泛型结果集 */
	private T resultData;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getResultCount() {
		return resultCount;
	}

	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}

	public T getResultData() {
		return resultData;
	}

	public void setResultData(T resultData) {
		this.resultData = resultData;
	}

}
