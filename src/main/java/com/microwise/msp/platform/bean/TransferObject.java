/**
 * 
 */
package com.microwise.msp.platform.bean;

import java.io.Serializable;

/**
 * <pre>
 * 数据传输对象 
 *   主要依据：step执行步骤 
 *   ①在client端  初始值   step=1
 *   ②在server端  执行成功 step=2，执行失败step=1，回送到client
 *   ③在client端  判断step=1,重复步骤①，step=2执行更新数据表相应dataVersion及日志表相应dataVersion
 * </pre>
 * 
 * @author heming
 * @since 2011-10-31
 */
public class TransferObject implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 需要同步的数据表名
	 */
	public String tableName;

	/**
	 * 一个数据表中需要同步的数据封装类
	 */
	public Object data = new Object();

	/**
	 * <pre>
	 * 数据表版本号  
	 *   ①client 
	 *    查询log_transfer同步日志表，判断
	 *    	首次同步，取(dataVersion=0)
	 *      非首次同步，取(dataVerion=0 or dataVersion大于表版本号) 
	 *    
	 *   ②server 
	 *    dataVersion=0  (新添加数据)      server进行insert 
	 *    dataVersion大于0(更新过的数据)   server进行update
	 *    	  
	 *    server	执行单条数据成功---单条数据版本号=表版本+1，加入返回对象
	 *          	执行单条数据失败---单条数据版本号=表版本+2，加入返回对象 
	 *          
	 *   ③server 表版本+1 返回client，client 更新日志表log_transfer--dataVersion
	 * </pre>
	 */
	public long dataVersion;

	public int step;

}
