package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.dao.SourceDao;
import com.microwise.msp.hardware.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sun.cong
 * @create 2017-11-15 13:36
 **/
@Service
@Scope("prototype")
@Transactional
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceDao sourceDao;

    /**
     * 通过sourceId查找sourceName
     *
     * @param systemFlag
     * @param sourceId
     * @return
     */
    @Override
    public String findSourceNameById(int systemFlag, String sourceId) {
        return sourceDao.findSourceNameById(systemFlag, sourceId);
    }
}
