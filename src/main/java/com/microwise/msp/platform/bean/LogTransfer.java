/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 *  同步状态表 m_log_transfer
 *  
 *  更新数据时，各表中的版本号需要先检索本表中的dataVersion值，在此基础上+1
 * </pre>
 * 
 * @author heming
 * @since 2011-11-01
 */
public class LogTransfer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 数据表唯一标识
	 */
	public String tableName;

	/**
	 * 数据版本
	 */
	public long dataVersion;

	/**
	 * 时间戳
	 */
	public Timestamp eventTime;

}
