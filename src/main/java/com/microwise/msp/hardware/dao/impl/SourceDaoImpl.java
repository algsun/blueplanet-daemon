package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.dao.SourceDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sun.cong
 * @create 2017-11-15 13:40
 **/
@Component
@Scope("prototype")
public class SourceDaoImpl extends BaseDaoImpl implements SourceDao {
    /**
     * 查找sourceName
     *
     * @param systemFlag
     * @param sourceId
     * @return
     */
    @Override
    public String findSourceNameById(int systemFlag, String sourceId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("systemFlag", systemFlag);
        params.put("sourceId", sourceId);
        return getSqlSession().selectOne("Source.findSourceNameById", params);
    }
}
