package com.microwise.msp.hardware.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microwise.msp.hardware.businessbean.DeviceFormulaBean;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 环境中间件http接口
 *
 * @author liuzhu
 * @date 2015-1-12
 */
public class OnlineHttpApiClient extends HttpApiClient  {
    public static final Logger log = LoggerFactory.getLogger(OnlineHttpApiClient.class);

    private HttpClient httpClient = new DefaultHttpClient();

    /**
     * 发送请求至onLine，下载设备公式
     *
     * @param sn 产品序列号
     */
    public List<DeviceFormulaBean> downloadFormula(String sn) throws IOException, JSONException {
        String result = request(SysConfig.galaxyOnLineUrl + "products/" + sn + "/formulas");
        Gson gson = new Gson();
        List<DeviceFormulaBean> deviceFormulaBeans = gson.fromJson(result, new TypeToken<List<DeviceFormulaBean>>() {
        }.getType());
        return deviceFormulaBeans;
    }

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }
}
