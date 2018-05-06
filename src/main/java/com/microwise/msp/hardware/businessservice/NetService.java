package com.microwise.msp.hardware.businessservice;

import com.microwise.msp.hardware.dao.NetManagerDao;
import com.microwise.msp.hardware.vo.NetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * NetInfo
 *
 * @author gaohui
 * @date 13-8-12 17:12
 */
@Component
@Scope("prototype")
public class NetService {

    @Autowired
    private NetManagerDao netManagerDao;

    /**
     *
     * @return
     * @deprecated 建议使用 findByType @gaohui 2014-04-14
     */
    @Transactional(readOnly = true)
    public List<NetInfo> findAllNetInfo() {
        return netManagerDao.getCommunications();
    }


    @Transactional(readOnly = true)
    public List<NetInfo> findByType(int type) {
        return netManagerDao.findByType(type);
    }
}
