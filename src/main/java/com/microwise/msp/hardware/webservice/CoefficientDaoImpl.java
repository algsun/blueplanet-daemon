package com.microwise.msp.hardware.webservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microwise.msp.hardware.dao.SqlMapClient2SqlSessionAdapter;
import com.microwise.msp.hardware.vo.EquipmentParameterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * 系数设置webService
 * 
 * @author xuexu
 * @since 2011-12-01
 */
@WebService
public class CoefficientDaoImpl extends SqlMapClient2SqlSessionAdapter implements
		CoefficientDao {

	private static Logger log = LoggerFactory.getLogger(CoefficientDaoImpl.class);

	@Override
	public boolean addCoefficient(@WebParam(name = "coeffList")
	String coeffList) {
		boolean bool = true;
		Gson gson = new Gson();
		try {
			List<EquipmentParameterBean> list = gson.fromJson(coeffList,
					new TypeToken<List<EquipmentParameterBean>>() {
					}.getType());
            for (EquipmentParameterBean coeff : list) {
                getSqlSession().insert("addSingleCoefficient", coeff);
            }
		} catch (DataAccessException e) {
			log.error("\n\n 添加系数(coeff.addCoefficient)出错 \n\n");
			bool = false;
		}
		return bool;
	}

	@Override
	public boolean deleteCoefficient(@WebParam(name = "coeff")
	String coeff) {
		Gson gson = new Gson();
		boolean bool = true;
		try {
			EquipmentParameterBean coe = gson.fromJson(coeff,
					EquipmentParameterBean.class);
			getSqlSession().delete("coeff.deleteCoefficient", coe);
		} catch (DataAccessException e) {
			log.error("\n\n 删除系数(deleteCoefficient)出错 \n\n");
			bool = false;
		}
		return bool;
	}

	@Override
	public boolean updateCoefficient(@WebParam(name = "coeffList")
	String coeffList) {
		Gson gson = new Gson();
		boolean bool = true;
		try {
			List<EquipmentParameterBean> list = gson.fromJson(coeffList,
					new TypeToken<List<EquipmentParameterBean>>() {
					}.getType());
			for (int i = 0; i < list.size(); i++) {
				EquipmentParameterBean coeff = list.get(i);
				getSqlSession().update("coeff.updateCoefficient", coeff);
			}
		} catch (DataAccessException e) {
			log.error("\n\n 修改系数(updateCoefficient)出错 \n\n");
			bool = false;
		}
		return bool;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String queryCoefficient(@WebParam(name = "coeff")
	String coeff) {
		System.out.println(coeff);
		Gson gson = new Gson();
		List<EquipmentParameterBean> coeffList = new ArrayList<EquipmentParameterBean>();
		try {
			EquipmentParameterBean coe = gson.fromJson(coeff,
					EquipmentParameterBean.class);
			coeffList = getSqlSession().selectList("coeff.queryCoefficient", coe);
		} catch (DataAccessException e) {
			log.error("\n\n 查询系数(queryCoefficient)出错 \n\n");
		}
		return gson.toJson(coeffList);
	}
}
