package com.microwise.msp.hardware.webservice;

/**
 * 常规设置webService
 * 
 * @author xuexu
 * @deprecated TODO 无用，删除之 @gaohui 2013-08-18
 */
public interface GeneralSetDao {

	/**
	 * 更新常规设置
	 * 
	 * @param datetimeForAvg
	 *            执行均峰值时间
	 * @param intervalForClient
	 *            客户端获取数据时间间隔
	 * @return
	 */
	public boolean updateGeneral(String datetimeForAvg, int intervalForClient);

	/**
	 * 查询常规设置
	 * 
	 */
	public String queryGeneral();
}
