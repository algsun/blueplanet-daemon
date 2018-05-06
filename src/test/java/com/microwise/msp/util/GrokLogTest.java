package com.microwise.msp.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.nflabs.grok.Grok;
import com.nflabs.grok.GrokException;
import com.nflabs.grok.Match;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author bastengao
 * @date 14-3-25 上午9:30
 */
public class GrokLogTest {

    @Test
    public void test(){

        Grok grok = new Grok();

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

        String log = "2014-03-25 09:31:11,742 [192.168.0.196:43741] <= 上行数据[01]-1-00315: 55AA0101032D0001003B017828BD08A000001EA0010E0319091F08A0040101A006009E0020045200211874A00203A2F62D9CD4";

        Match match = grok.match(log);
        match.captures();

        Map<String, Object> map = match.toMap();

        Assert.assertNotNull(map.get("timestamp"));
        Assert.assertEquals("192.168.0.196:43741", map.get("gateway"));
        Assert.assertNotNull(map.get("packetDesc"));
        Assert.assertEquals("上行数据", map.get("packetDesc"));
        Assert.assertEquals(1, map.get("packetType"));
        Assert.assertEquals(1, map.get("deviceType"));
        Assert.assertEquals(315, map.get("deviceId"));
        Assert.assertNotNull(map.get("message"));
        Assert.assertNotNull(map.get("packet"));
        Assert.assertEquals("55AA0101032D0001003B017828BD08A000001EA0010E0319091F08A0040101A006009E0020045200211874A00203A2F62D9CD4", map.get("packet"));

    }

}
