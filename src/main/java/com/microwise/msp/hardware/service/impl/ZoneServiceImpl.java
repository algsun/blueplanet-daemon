package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.dao.ZoneDao;
import com.microwise.msp.hardware.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sun.cong
 * @create 2017-11-15 14:18
 **/
@Service
@Scope("prototype")
@Transactional
public class ZoneServiceImpl implements ZoneService {
    @Autowired
    private ZoneDao zoneDao;

    /**
     * 通过id查找区域或者文物名称
     *
     * @param systemFlag
     * @param zoneId
     * @return
     */
    @Override
    public String findZoneNameById(int systemFlag, String zoneId) {
        return zoneDao.findZoneNameById(systemFlag, zoneId);
    }
}
