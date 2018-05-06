package com.microwise.msp.hardware.dao;

import com.microwise.msp.hardware.businessbean.Office;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

/**
 * Created by lijianfei on 2017/12/15.
 *
 * @author li.jianfei
 * @since 2017/12/15
 */
public interface OfficeDao {

    @Select("select id, office_name as officeName, create_date as createDate, del_flag as delFlat from sys_office where id=#{id}")
    public Office findOffice(String id);
}
