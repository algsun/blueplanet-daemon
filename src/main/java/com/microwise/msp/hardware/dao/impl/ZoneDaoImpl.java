package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.Zone;
import com.microwise.msp.hardware.dao.ZoneDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bastengao
 * @date 14-3-19 下午4:29
 */
@Component
@Scope("prototype")
public class ZoneDaoImpl extends BaseDaoImpl implements ZoneDao {

    @Override
    public Zone findZone(String id) {
        return getSqlSession().selectOne("Zone.findZone", id);
    }

    /**
     * 查询区域名称/文物名称
     *
     * @param systemFlag
     * @param zoneId
     * @return
     */
    @Override
    public String findZoneNameById(int systemFlag, String zoneId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("systemFlag", systemFlag);
        params.put("zoneId", zoneId);
        return getSqlSession().selectOne("Zone.findZoneNameById", params);
    }
}

