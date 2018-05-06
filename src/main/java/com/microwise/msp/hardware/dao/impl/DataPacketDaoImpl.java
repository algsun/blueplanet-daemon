/**
 * @author he.ming
 * @since 2013-1-15
 */
package com.microwise.msp.hardware.dao.impl;

import com.microwise.msp.hardware.businessbean.DataPacket;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.dao.DataPacketDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 原始数据包daoImpl
 *
 * @author he.ming
 * @since 2013-1-15
 */
public class DataPacketDaoImpl extends BaseDaoImpl implements DataPacketDao {

    private static final Logger log = LoggerFactory.getLogger(DataPacketDaoImpl.class);

    @Override
    public boolean createPacketTable(String nodeId) {
        boolean isTrue = false;
        try {
            getSqlSession().update("DataPacket.createPacketTable", nodeId);
            isTrue = true;
        } catch (DataAccessException e) {
            log.error("\n\n 创建节点原始数据缓存表(createPacketTable(" + nodeId
                    + "))出错! \n\n", e);
        }
        return isTrue;
    }

    @Override
    public boolean isExistPacket(DeviceBean deviceBean) {
        boolean isExist = false;
        try {
            int exist = (Integer) getSqlSession().selectOne("DataPacket.isExistPacket", deviceBean);
            if (exist > 0) {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.error("\n\n 判断当前数据包:isExistPacket(" + deviceBean.packet
                    + "),在库中是否已经存在出错! \n\n", e);
        }
        return isExist;
    }

    @Override
    public boolean savePacket(DeviceBean deviceBean) {
        boolean isTrue = false;
        try {
            getSqlSession().insert("DataPacket.savePacket", deviceBean);
            isTrue = true;
        } catch (DataAccessException e) {
            isTrue = false;
            log.error("\n\n保存原始数据包savePacket(" + deviceBean.packet
                    + ")出错！\n\n", e);
        }
        return isTrue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DataPacket> findPackets(String deviceId, Date receiveTime) {
        List<DataPacket> rList = new ArrayList<DataPacket>();
        String stamp = currentHourStartStr(receiveTime);
        Map<String, Object> parmeterMap = new HashMap<String, Object>();
        parmeterMap.put("nodeId",deviceId);
        parmeterMap.put("stamp",stamp);
        try {
            rList = getSqlSession().selectList("DataPacket.findPackets", parmeterMap);
        } catch (DataAccessException e) {
            log.error("findPackets(),error", e);
        }
        return rList;

    }

    /**
     * 当前时间小时的开始
     *
     * @param time
     * @return
     * @author he.ming
     * @date May 29, 2012
     */
    private static String currentHourStartStr(Date time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(time);
    }

    /**
     * 当前时间小时的结束
     *
     * @param time
     * @return
     * @author he.ming
     * @date May 29, 2012
     */
    private static String currentHourEndStr(Date time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:59:59").format(time);
    }

    /**
     * 返回当前小时的起始时间 ，比如 "2012-05-18 13:24:56" => "2012-05-18 13:00:00"
     *
     * @param time
     * @return
     */
    public static Calendar currentHourStart(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar;
    }

    /**
     * 返回当前小时的结束时间 ，比如 "2012-05-18 13:24:56" => "2012-05-18 13:59:59"
     *
     * @param time
     * @return
     */
    public static Calendar currentHourEnd(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }
}
