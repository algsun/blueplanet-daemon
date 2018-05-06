package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.MethodType;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.google.common.collect.Lists;
import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.hardware.handler.codec.v13.DataPacketV13Parser;
import com.microwise.msp.hardware.handler.codec.v13.GatewayHeartBeatPacketParser;
import com.microwise.msp.hardware.handler.codec.v13.RelayRoutePacketParser;
import com.microwise.msp.hardware.handler.codec.v30.CmdRespPacketParser;
import com.microwise.msp.hardware.handler.codec.v30.DataPacketV30Parser;
import com.microwise.msp.hardware.handler.codec.v30.RequestPacketV30Parser;
import com.microwise.msp.hardware.handler.codec.v30.StatusPacketParser;
import com.microwise.msp.util.StringUtil;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;


/**
 * 解析
 *
 * @author gaohui
 * @date 13-11-14 21:27
 */
@Component
@Scope("prototype")
@Route("/struts")
public class PacketParseController {
    public static final Logger log = LoggerFactory.getLogger(PacketParseController.class);

    // 包解析器集合
    private static final List<PacketParser> PACKET_PARSERS = Collections.unmodifiableList(Lists.<PacketParser>newArrayList(
            // v3.0
            new DataPacketV30Parser(),
            new StatusPacketParser(),
            new RequestPacketV30Parser(),
            new CmdRespPacketParser(),

            // v1.3
            new DataPacketV13Parser(),
            new GatewayHeartBeatPacketParser(),
            new RelayRoutePacketParser()
    ));

    // in
    /**
     * 原始 数据包
     */
    private String pack;

    //out
    /**
     * 消息
     */
    private String message;

    private Boolean isSuccess = false;

    private Packet packet = null;

    private TemplateHashModel statics = BeansWrapper.getDefaultInstance().getStaticModels();

    @Route(value = "/parse", method = MethodType.GET)
    public String index() {
        return Results.ftl("/pages/parse");
    }

    @Route(value = "/parse", method = MethodType.POST)
    public String toParse() {
        try {
            PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
            Packet packet = packetSplitter.split(ByteBuffer.wrap(StringUtil.fromHexs(pack)));
            if (packet == null) {
                message = "数据包解析失败！";
                return Results.ftl("/pages/parse");
            }
            parse(packet);
            if (!Packets.checkCRC(packet)) {
                isSuccess = false;
                message = "CRC验证错误！";
            }
        } catch (Exception e) {
            message = "数据包解析失败！";
            log.error("", e);
        }
        return Results.ftl("/pages/parse");
    }

    private void parse(Packet packet) {
        for (PacketParser<Packet> parser : PACKET_PARSERS) {
            if (parser.isParseable(packet)) {
                try {
                    isSuccess = true;
                    this.packet = parser.parse(packet);
                    break;
                } catch (Throwable e) {
                    message = "数据包解析异常: " + e.getMessage();
                    return;
                }
            }
        }

        if (this.packet == null) {
            message = "未知的数据包类型！";
            return;
        }
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public TemplateHashModel getStatics() {
        return statics;
    }
}
