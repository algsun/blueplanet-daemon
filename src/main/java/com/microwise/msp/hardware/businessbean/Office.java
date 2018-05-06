package com.microwise.msp.hardware.businessbean;

import lombok.Data;

import java.util.Date;

/**
 * 终结者系统机构信息
 *
 * @author li.jianfei
 * @since 2017/12/15
 */
@Data
public class Office {
    /**
     * 机构编号
     */
    private String id;

    /**
     * 机构名称
     */
    private String officeName;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 删除标记
     */
    private Boolean delFlag;
}
