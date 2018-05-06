package com.microwise.msp.hardware.vo;


/**
 * 通讯信息Bean
 * 
 * @author he.ming
 * @since Jan 31, 2013
 */
public class NetInfo {
    // UDP
    public static final int MODE_UDP = 2;
    // TCP
    public static final int MODE_TCP = 3;

	/** id */
	private int id;
	/** 远程ip地址 TCPClient通讯模式使用 */
	private String radds;
	/**
     * 远程端口 TCPClient通讯模式使用
     * @deprecated @gaohui 2014-04-14
     */
	private int rport;
	/**
     * 本地端口 UDP通讯模式使用
     */
	private int lport;
	/**
     * 串口号 串口通讯使用
     * @deprecated @gaohui 2014-04-14
     */
	private String sport;
	/**
     * 波特率 串口通讯使用
     * @deprecated @gaohui 2014-04-14
     */
	private int brate;
	/** 通讯模式 1串口，2UDP,3TCP */
	private int model;
	/** 连接状态 0未连接，1正在连接，2已连接，3正在断开连接 */
	private int state;
    /** 站点ID, v1.3 协议使用，v3协议忽略 */
    private String siteId;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLport() {
		return lport;
	}

	public void setLport(int lport) {
		this.lport = lport;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {

        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "NetInfo{" +
                "id=" + id +
                ", lport=" + lport +
                ", model=" + model +
                ", state=" + state +
                ", siteId='" + siteId + '\'' +
                '}';
    }
}
