package com.microwise.msp.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.microwise.msp.hardware.common.SysConfig;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bastengao
 * @date 14-4-28 下午5:36
 */
@Deprecated
public class SmsSender {
    public static final Logger log = LoggerFactory.getLogger(SmsSender.class);

    /**
     * Post 方式发送短信
     *
     * @param content 短信内容
     * @param mobile  手机号码，多个手机号码通过逗号分隔
     * @return 短信是否发送成功
     */
    public static boolean send(String content, String mobile, Map<String, Object> properties) {
        boolean isSuccess = false;
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(SysConfig.getInstance().getSmsUrl());
//
//        try {
//
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("customerApp", SysConfig.getInstance().getCustomerApp()));
//            nameValuePairs.add(new BasicNameValuePair("content", content));
//            nameValuePairs.add(new BasicNameValuePair("numbers", mobile));
//            if(properties != null){
//                Gson gson = new Gson();
//                String propertiesJson = gson.toJson(properties).toString();
//                nameValuePairs.add(new BasicNameValuePair("propertys", propertiesJson));
//            }
//            nameValuePairs.add(new BasicNameValuePair("sendPassword", SysConfig.getInstance().getSendPassword()));
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//
//            HttpResponse response = httpclient.execute(httpPost);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                //读返回数据
//                String result = EntityUtils.toString(response.getEntity());
//                Type type = new TypeToken<Map<String, Boolean>>() {
//                }.getType();
//                Gson gson = new Gson();
//                Map<String, Boolean> map = gson.fromJson(result, type);
//                isSuccess = map.get("success");
//            } else {
//                isSuccess = false;
//            }
//        } catch (ClientProtocolException e) {
//            log.error("", e);
//        } catch (IOException e) {
//            log.error("", e);
//        }
        return isSuccess;
    }
}
