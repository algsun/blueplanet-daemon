package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.dao.QCMDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * QCM实现
 *
 * @author liuzhu
 * @date 2016-1-6
 */

@Component
@Scope("prototype")
public class QCMDaoImpl extends BaseDaoImpl implements QCMDao {

    @Override
    public void insertReplaceSensor(String locationId, Date replaceDate) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("replaceDate", replaceDate);
        getSqlSession().insert("QCM.insertReplaceSensor", paramMap);
    }

    @Override
    public boolean isExist(String locationId, Date replaceDate) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("replaceDate", replaceDate);
        Integer count = getSqlSession().selectOne("QCM.findReplaceSensorCount",paramMap);
        if (count != 0) {
            return true;
        } else {
            return false;
        }
    }
}
