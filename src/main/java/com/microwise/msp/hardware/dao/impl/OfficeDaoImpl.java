package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.Office;
import com.microwise.msp.hardware.dao.OfficeDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/12/15.
 */
@Component
@Scope("prototype")
public class OfficeDaoImpl extends BaseDaoImpl implements OfficeDao{

    @Override
    public Office findOffice(String id) {
        return getSqlSession().selectOne("Office.findOffice",id);
    }
}
