package com.microwise.msp.util;

/**
 * 所有表定义
 *
 * @author heming
 * @since 2011-11-16
 */
public class Tables {

    /**
     * 数据库相关表
     *
     * @author heming
     * @since 2011-12-03
     */
    public enum tableInfos {
        m_nodeinfo, node, m_nodeinfomemory, m_nodesensor, m_avgdata, m_tbl_lxh_acc, m_tbl_rb_day_acc, m_tbl_rb_hour_acc, m_windrose, mapping_area_site, mapping_site_node, t_monitoringStation_info, t_node_monitoringStation_info, m_historydatacount
    }

    /**
     * 节点信息表
     */
    public static final String nodeinfo = "m_nodeinfo";
    /**
     * 节点
     */
    public static final String node = "node";

    /**
     * 节点链路信息表
     */
    public static final String deviceLink = "m_device_";

    /**
     * 节点内存，实时数据表
     */
    public static final String nodeinfomemory = "m_nodeinfomemory";
    /**
     * 节点_端口_传感对应表
     */
    public static final String nodesensor = "m_nodesensor";
    /**
     * 均峰值
     */
    public static final String avgdata = "m_avgdata";
    /**
     * 日照量
     */
    public static final String tbl_lxh_acc = "m_tbl_lxh_acc";
    /**
     * 日降雨量
     */
    public static final String tbl_rb_day_acc = "m_tbl_rb_day_acc";
    /**
     * 小时降雨量
     */
    public static final String tbl_rb_hour_acc = "m_tbl_rb_hour_acc";
    /**
     * 风向玫瑰图
     */
    public static final String windrose = "m_windrose";
    /**
     * 监测站（监测中心）级别 对应信息表
     */
    public static final String mapping_area_site = "mapping_area_site";
    /**
     * 监测站（或监测中心）_节点 对应信息表
     */
    public static final String mapping_site_node = "mapping_site_node";

    /**
     * 监测点====前台
     */
    public static final String monitoringStation_info = "t_monitoringStation_info";
    /**
     * 设备(节点)_监测点对应信息====前台
     */
    public static final String node_monitoringStation_info = "t_node_monitoringStation_info";

    /**
     * 历史数据索引表
     */
    public static final String historydatacount = "m_historydatacount";

    /**
     * 站点组表
     */
    public static final String logicGroup = "t_logicgroup";

    /**
     *
     */
    public static final String syncTables = "o_sync_tables";

}
