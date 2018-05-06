/**
 * 
 */
package com.microwise.msp.platform.bean;

/**
 * <pre>
 * 数据库对象
 * table
 * view
 * stored procs
 * functions
 * triggers
 * events
 * </pre>
 * 
 * @author heming
 * @since 2011-12-27
 */
public class DataBaseObject {

	public final static String view = "VIEW";
	public final static String table = "BASE TABLE";

	/**
	 * 对象名称
	 */
	public String table_name;
	/**
	 * 对象类型
	 */
	public String table_type;

	/**
	 * 数据库名称
	 */
	public String table_schema;

	public DataBaseObject() {
	}

}
