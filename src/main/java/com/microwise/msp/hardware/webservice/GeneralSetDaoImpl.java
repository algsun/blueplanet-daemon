package com.microwise.msp.hardware.webservice;

import com.google.gson.Gson;
import com.microwise.msp.hardware.dao.SqlMapClient2SqlSessionAdapter;
import com.microwise.msp.hardware.vo.General;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.jws.WebService;

/**
 * 常规设置webService
 * 
 * @author xuexu
 * 
 */
@WebService
public class GeneralSetDaoImpl extends SqlMapClient2SqlSessionAdapter implements
		GeneralSetDao {

	private static Logger log = LoggerFactory.getLogger(GeneralSetDaoImpl.class);

	@Override
	public boolean updateGeneral(String datetimeForAvg, int intervalForClient) {
		boolean bool = true;
		try {
			General gen = new General();
			gen.setAvgPeakTime(datetimeForAvg);
			gen.setIntervalForClient(intervalForClient);
			getSqlSession().update("general.updateGeneral", gen);
		} catch (DataAccessException e) {
			log.error("\n\n 更新常规设置(updateGeneral)出错 \n\n");
			bool = false;
		}
		return bool;
	}

	@Override
	public String queryGeneral() {
		Gson gson = new Gson();
		General gen = new General();
		try {
			gen = (General) getSqlSession().selectOne("general.queryGeneral");
		} catch (DataAccessException e) {
			log.error("\n\n 查询常规设置(queryGeneral)出错 \n\n");
		}
		return gson.toJson(gen);
	}

}
