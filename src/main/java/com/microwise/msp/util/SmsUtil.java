package com.microwise.msp.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lijianfei on 15/8/13.
 */
public class SmsUtil {
    //修改为您的apikey.apikey可在官网（http://www.yuanpian.com)登录后用户中心首页看到
    // APIKEY
    public static final String API_KEY = "ef6042a2787d223e87a8718d0c5ba776";

    // 查账户信息的http地址
    private static String URI_GET_USER_INFO = "http://yunpian.com/v1/user/get.json";

    //通用发送接口的http地址
    private static String URI_SEND_SMS = "http://yunpian.com/v1/sms/send.json";

    // 模板发送接口的http地址
    private static String URI_TPL_SEND_SMS = "http://yunpian.com/v1/sms/tpl_send.json";

    // 发送语音验证码接口的http地址
    private static String URI_SEND_VOICE = "http://yunpian.com/v1/voice/send.json";

    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";

    public static final String PROXY_HOST_NAME = "sms.smart-museum.cn";
    public static final int PROXY_HOST_PROT = 81;


    public static void main(String[] args) throws IOException, URISyntaxException {
        //修改为您要发送的手机号

        /**************** 查账户信息调用示例 *****************/
        System.out.println(SmsUtil.getUserInfo());

//        /**************** 使用通用接口发短信(推荐) *****************/
//        //设置您要发送的内容(内容必须和某个模板匹配。以下例子匹配的是系统提供的1号模板）
//        String text = "【云片网】您的验证码是1234";
//        //发短信调用示例
//        System.out.println(SmsUtil.sendSms(text, mobile));

//        /**************** 使用模板接口发短信(不推荐，建议使用通用接口) *****************/
//        设置模板ID，如使用1号模板:【#company#】您的验证码是#code#
//        long tpl_id = 1;
//        //设置对应的模板变量值
//        //如果变量名或者变量值中带有#&=%中的任意一个特殊符号，需要先分别进行urlencode编码
//        //如code值是#1234#,需作如下编码转换
//        String codeValue = URLEncoder.encode("#1234#", ENCODING);
//        String tpl_value = "#code#=" + codeValue + "&#company#=云片网";
//        //模板发送的调用示例
//        System.out.println(SmsUtil.tplSendSms(apikey, tpl_id, tpl_value, mobile));
//
//        /**************** 使用接口发语音验证码 *****************/
//        String code = "1234";
//        System.out.println(SmsUtil.sendVoice(apikey, mobile, code));
    }

    /**
     * 取账户信息
     *
     * @return json格式字符串
     * @throws IOException
     */
    public static String getUserInfo() throws IOException, URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", API_KEY);
        return post(URI_GET_USER_INFO, params);
    }

    /**
     * 通用接口发短信
     *
     * @param text   　短信内容
     * @param mobile 　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String sendSms(String text, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", API_KEY);
        params.put("text", text);
        params.put("mobile", mobile);
        return post(URI_SEND_SMS, params);
    }

    /**
     * 通过模板发送短信(不推荐)
     *
     * @param tpl_id    　模板id
     * @param tpl_value 　模板变量值
     * @param mobile    　接受的手机号
     * @return json格式字符串
     * @throws IOException
     */
    public static String tplSendSms(long tpl_id, String tpl_value, String mobile) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", API_KEY);
        params.put("tpl_id", String.valueOf(tpl_id));
        params.put("tpl_value", tpl_value);
        params.put("mobile", mobile);
        return post(URI_TPL_SEND_SMS, params);
    }

    /**
     * 通过接口发送语音验证码
     *
     * @param mobile 接收的手机号
     * @param code   验证码
     * @return
     */
    public static String sendVoice(String mobile, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", API_KEY);
        params.put("mobile", mobile);
        params.put("code", code);
        return post(URI_SEND_VOICE, params);
    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();

        // 依次是目标请求地址，端口号,协议类型
        HttpHost target = HttpHost.create(url);
        // 依次是代理地址，代理端口号，协议类型
        HttpHost proxy = new HttpHost(PROXY_HOST_NAME, PROXY_HOST_PROT, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            method.setConfig(config);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(target, method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }
}
