package com.microwise.msp.hardware.webservice;

/**
 * 公式系数设置webService
 * 
 * @author xuexu
 * 
 */
public interface CoefficientDao {

	/**
	 * 添加系数
	 * 
	 * @param coeffList
	 *            系数集合
	 * @return
	 */
	public boolean addCoefficient(String coeffList);

	/**
	 * 删除系数
	 * 
	 * @param coeff
	 *            系数
	 * @return
	 */
	public boolean deleteCoefficient(String coeff);

	/**
	 * 修改系数
	 * 
	 * @param coeff
	 *            系数
	 * @return
	 */
	public boolean updateCoefficient(String coeffList);

	/**
	 * 查询系数
	 * 
	 * @param coeff
	 *            系数
	 * @return
	 */
	public String queryCoefficient(String coeff);
}
