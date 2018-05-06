package com.microwise.msp.hardware.businessservice;

import com.google.common.io.CharStreams;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.SensorPhysicalBean;
import com.microwise.msp.hardware.common.Defines;
import com.microwise.msp.hardware.dao.RouteHistoryDao;
import com.microwise.msp.hardware.vo.CoordinatesVO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

/**
 * 车辆service
 *
 * @author sun.cong
 * @date 18-3-26
 */
@Component
@Scope("prototype")
public class RouteHistoryService {

    @Autowired
    private CarService carService;
    @Autowired
    private RouteHistoryDao routeHistoryDao;

    private static final Logger logger = LoggerFactory.getLogger(RouteHistoryService.class);

    /**
     * 插入routeHistory
     *
     * @param deviceBean
     */
    public void insert(DeviceBean deviceBean) {
        try {
            int carId = carService.findCarIdByLocationId(deviceBean.locationId);
            Map sensorMap = deviceBean.getSensorData();
            SensorPhysicalBean lng = (SensorPhysicalBean) sensorMap.get(Defines.LONGITUDE);
            SensorPhysicalBean lat = (SensorPhysicalBean) sensorMap.get(Defines.LATITUDE);
            CoordinatesVO coordinates = new CoordinatesVO();
            coordinates.setLongitude(Double.parseDouble(lng.getValueStr()));
            coordinates.setLatitude(Double.parseDouble(lat.getValueStr()));
            coordinates = gpsConvertToGoogleXY(coordinates);
            routeHistoryDao.insert(carId, coordinates.getLongitude(), coordinates.getLatitude(), deviceBean.timeStamp);
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    /**
     * 1.发送HTTP请求进行坐标转化，GPS坐标转GOOGLE坐标
     * 2.对结果进行base64解码
     *
     * @param coordinates 经纬度对象
     * @return 转换后的经纬度对象
     */
    public CoordinatesVO gpsConvertToGoogleXY(CoordinatesVO coordinates) {
        String httpUrl = "http://api.map.baidu.com/ag/coord/convert?x=" + coordinates.getLongitude() + "&y="
                + coordinates.getLatitude() + "&from=0&to=2&mode=1";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(httpUrl);
            HttpResponse response = httpClient.execute(get);
            InputStreamReader reader = new InputStreamReader(response
                    .getEntity().getContent(), "utf-8");
            String resultText = CharStreams.toString(reader);

            logger.info("经纬度转换" + resultText);

            JSONObject jsonObject = new JSONObject(resultText.substring(1, resultText.length() - 1));
            String base64Longitude = (String) jsonObject.get("x");
            String base64Latitude = (String) jsonObject.get("y");
            BASE64Decoder base64Decoder = new BASE64Decoder();

            coordinates.setLongitude(Double.parseDouble(new String(base64Decoder.decodeBuffer(base64Longitude))));
            coordinates.setLatitude(Double.parseDouble(new String(base64Decoder.decodeBuffer(base64Latitude))));
        } catch (Exception e) {
            coordinates = null;
            e.printStackTrace();
        }
        return coordinates;
    }
}
