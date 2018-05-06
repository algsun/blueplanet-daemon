package com.microwise.msp.hardware.service.impl;

import com.microwise.msp.hardware.businessbean.DataFile;
import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.hardware.dao.AnalysisDao;
import com.microwise.msp.hardware.handler.AbstractServerHandler;
import com.microwise.msp.hardware.handler.codec.*;
import com.microwise.msp.hardware.handler.codec.v30.DataV30Packet;
import com.microwise.msp.hardware.netlink.ChannelAttributeCache;
import com.microwise.msp.hardware.service.AnalysisDataFileService;
import com.microwise.msp.util.AppContext;
import com.microwise.msp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiedeng
 * @date 14-7-28
 */
@Component
public class AnalysisDataFileServiceImpl extends AbstractServerHandler implements AnalysisDataFileService, ApplicationContextAware {

    public static final Logger log = LoggerFactory.getLogger(AnalysisDataFileServiceImpl.class);

    private ApplicationContext applicationContext;

    /**
     * 未解析标识
     */
    private final static int ANALYSIS_NON_COMPLETE = 0;

    /**
     * 解析完成标识
     */
    private final static int ANALYSIS_COMPLETE = 1;

    @Autowired
    private AnalysisDao analysisDao;

    public void handleFile() {
        String baseUrl = ((WebApplicationContext) applicationContext).getServletContext().getRealPath("/");

        List<DataFile> dataFiles = analysisDao.getFileRecords(ANALYSIS_NON_COMPLETE);
        if (dataFiles == null || dataFiles.isEmpty()) {
            log.info("木卫一解析数据,没有需要解析的文件");
            return;
        }
        for (DataFile dataFile : dataFiles) {
            File file = new File(baseUrl, SysConfig.datafileDirectory + File.separator + dataFile.getFilename());
            if (file.exists()) {
                convertPacket(getPacket(file));
                //修改文件标记为已经解析
                analysisDao.updateDataFileRecord(file.getName(), ANALYSIS_COMPLETE);
            } else {
                log.info("木卫一解析数据,解析的数据文件不存在");
            }
        }
    }

    public void handleFile(String filename) {
        String baseUrl = ((WebApplicationContext) applicationContext).getServletContext().getRealPath("/");
        File file = new File(baseUrl, SysConfig.datafileDirectory + File.separator + filename);
        if (file.exists()) {
            convertPacket(getPacket(file));
            //修改文件标记为已经解析
            analysisDao.updateDataFileRecord(file.getName(), ANALYSIS_COMPLETE);
        } else {
            log.info("木卫一解析的数据文件不存在");
        }
    }

    private OriginalPacket getPacket(File file) {
        OriginalPacket originalPacket = new OriginalPacket();
        FileReader reader = null;
        BufferedReader br = null;
        List<String> packets = new ArrayList<String>();
        try {
            reader = new FileReader(file);
            br = new BufferedReader(reader);
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                if (StringUtils.isNotBlank(buffer.trim())) {
                    packets.add(buffer.trim());
                }
            }
        } catch (Exception e) {
            log.error("木卫一解析数据文件失败", e);
        } finally {
            try {
                br.close();
                reader.close();
            } catch (IOException e) {
                log.error("木卫一解析数据文件,关闭文件流失败", e);
            }
        }
        originalPacket.setLocationId(file.getName());
        originalPacket.setPackets(packets);
        return originalPacket;
    }

    private void convertPacket(OriginalPacket originalPacket) {
        PacketSplitter packetSplitter = new MultiVersionPacketSplitter();
        for (int i = 0; i < originalPacket.getPackets().size(); i++) {
            String pack = originalPacket.getPackets().get(i);
            Packet packet = packetSplitter.split(hexString2ByteBuffer(pack));
            if (i == 0) {
                packet.setFirstPacket(true);
            }
            if (i == (originalPacket.getPackets().size() - 1)) {
                packet.setLastPacket(true);
            }
            try {
                InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
                InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, 0);
                packet.setLocationId(originalPacket.getLocationId());
                packet.setRemote(inetSocketAddress);
            } catch (UnknownHostException e) {
                log.error("未知的主机地址", e);
            }
            dispatchPacket(packet);
        }
    }

    private void dispatchPacket(Packet packet) {
        Packet parsedPacket = null;
        for (PacketParser<Packet> parser : PACKET_PARSERS) {
            if (parser.isParseable(packet)) {
                try {
                    parsedPacket = parser.parse(packet);
                    parsedPacket.setUpload(true);
                    parsedPacket.setLocationId(packet.getLocationId());
                    parsedPacket.setFirstPacket(packet.isFirstPacket());
                    parsedPacket.setLastPacket(packet.isLastPacket());
                    break;
                } catch (Throwable e) {
                    log.error("解析包异常", e);
                }
            }
        }
        if (parsedPacket == null) {
            log.error("木卫一数据包解析异常");
        } else if (parsedPacket.getVersion() != Versions.V_3) {
            log.error("木卫一数据包解析 XXX 未知包类型: {}", StringUtil.toHex(packet.getPacket()));
        } else {
            DataV30Packet dataPacket = (DataV30Packet) parsedPacket;
            Packets.logData(dataPacket);
            agentWarden.onPacketReceived(parsedPacket);
        }
    }

    private ByteBuffer hexString2ByteBuffer(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        byte[] bytes = new byte[length];
        char[] chars = hexString.toCharArray();
        for (int i = 0; i < length; i++) {
            int position = i * 2;
            bytes[i] = (byte) (char2byte(chars[position]) << 4 | char2byte(chars[position + 1]));
        }
        return ByteBuffer.wrap(bytes);
    }

    private byte char2byte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    @Override
    protected ChannelAttributeCache getChannelAttributeCache() {
        return null;
    }

    protected class OriginalPacket {
        String locationId;
        List<String> packets;

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public List<String> getPackets() {
            return packets;
        }

        public void setPackets(List<String> packets) {
            this.packets = packets;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
