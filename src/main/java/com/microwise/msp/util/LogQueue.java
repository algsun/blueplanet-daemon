package com.microwise.msp.util;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.nflabs.grok.Grok;
import com.nflabs.grok.GrokException;
import com.nflabs.grok.Match;

import java.io.IOException;
import java.util.*;

/**
 * 日志队列, 保留最近的一部分日志
 *
 * @author bastengao
 * @date 14-3-5 上午11:25
 */
public class LogQueue {

    private static LogQueue instance = new LogQueue();
    private final static int QUEUE_LENGTH = 200;

    // 目前 200 条
    private EvictingQueue<StructureLog> logs = EvictingQueue.create(QUEUE_LENGTH);
    private final Grok grok;

    // 网关
    private final Set<String> gateways = new HashSet<String>();
    // 节点号
    private final Set<String> deviceIds = new HashSet<String>();


    private LogQueue() {
        grok = new Grok();

        try {
            grok.addPatternFromReader(Resources.newReaderSupplier(
                    Resources.getResource("patterns.txt"), Charsets.UTF_8
            ).getInput());
            grok.addPattern("DEVICEID", "[0-9\\s]{5}");
            grok.addPattern("DATA_PACKET", "55AA[0-9A-F]+");
            grok.compile("%{TIMESTAMP_ISO8601:timestamp} \\[%{HOSTPORT:gateway}\\] (<=|=>) %{NOTSPACE:packetDesc}\\[%{BASE16NUM:packetType}\\][-]%{INT:deviceType}[-]%{DEVICEID:deviceId}[:] %{GREEDYDATA:message}%{DATA_PACKET:packet}");
        } catch (GrokException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static LogQueue getInstance() {
        return instance;
    }

    /**
     * 追加日志
     *
     * @param log
     */
    public synchronized void append(int level, String log) {
        Match match = grok.match(log);
        match.captures();
        StructureLog structureLog = new StructureLog(level, log, match.toMap());
        logs.add(structureLog);

        if (structureLog.simple().getGateway() != null) {
            gateways.add(structureLog.simple().getGateway());
        }

        if (structureLog.simple().getDeviceId() != null) {
            deviceIds.add(structureLog.simple().getDeviceId());
        }
    }

    /**
     * 返回最新日志的快照
     *
     * @return
     */
    public List<SimpleLog> logs() {
        return Lists.transform(new ArrayList<StructureLog>(logs), new Function<StructureLog, SimpleLog>() {
            @Override
            public SimpleLog apply(StructureLog log) {
                return log.simple();
            }
        });
    }

    public Set<String> getGateways() {
        return gateways;
    }

    public Set<String> getDeviceIds() {
        return deviceIds;
    }

    public static class SimpleLog {
        private int level;

        private String rawLog;

        private String gateway;

        private String deviceId;

        private String packet;

        public SimpleLog(int level, String rawLog, String gateway, String deviceId, String packet) {
            this.level = level;
            this.rawLog = rawLog;
            this.gateway = gateway;
            this.deviceId = deviceId;
            this.packet = packet;
        }

        public int getLevel() {
            return level;
        }

        public String getRawLog() {
            return rawLog;
        }

        public String getGateway() {
            return gateway;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public String getPacket() {
            return packet;
        }
    }

    public static class StructureLog {
        private int level;

        private String rawLog;

        private Map<String, Object> data;

        private SimpleLog simpleLog;

        public StructureLog(int level, String rawLog, Map<String, Object> data) {
            this.level = level;
            this.rawLog = rawLog;
            this.data = data;
        }

        public int getLevel() {
            return level;
        }

        public String getRawLog() {
            return rawLog;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public SimpleLog simple() {
            if (simpleLog == null) {
                String gateway = String.valueOf(this.getData().get("gateway"));
                String deviceId = String.valueOf(this.getData().get("deviceId"));
                String packet = (String) this.getData().get("packet");
                simpleLog = new SimpleLog(level, rawLog, gateway, deviceId, packet);
            }

            return simpleLog;
        }
    }
}

