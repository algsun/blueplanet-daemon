package com.microwise.msp.hardware.common;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 提供 HTTP
 *
 * @author liuzhu
 * @date 2015-1-12
 */
public abstract class HttpApiClient {

    /**
     * 发起 get 请求，并以 json object 的方式解析响应内容
     *
     * @param url
     * @return
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    protected String request(String url) throws IOException,
            JSONException {
        InputStream input = null;
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = getHttpClient().execute(get);
            input = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(response
                    .getEntity().getContent(), "utf-8");
            String resultText = CharStreams.toString(reader);
            return new JSONObject(resultText).getString("data");
        } finally {
            Closeables.close(input, true);
        }
    }


    /**
     * 发起 put 请求，并以 json object 的方式解析响应内容
     *
     * @param put
     * @return
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    protected JSONObject requestAndParse(HttpPut put) throws IOException,
            JSONException {
        InputStream input = null;
        try {
            HttpResponse response = getHttpClient().execute(put);
            input = response.getEntity().getContent();
            response.addHeader("Content-type", "application/x-www-form-urlencoded");
            InputStreamReader reader = new InputStreamReader(response
                    .getEntity().getContent(), "utf-8");
            String resultText = CharStreams.toString(reader);
            return new JSONObject(resultText);
        } finally {
            Closeables.close(input, true);
        }
    }

    public String post(String url, String jsonStr) throws IOException, JSONException {
        HttpPost post = null;
        InputStream input = null;
        try {
            if (!Strings.isNullOrEmpty(jsonStr)) {
                post = new HttpPost(url);
                StringEntity s = new StringEntity(jsonStr,"UTF-8");
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                post.setEntity(s);
            }

            HttpResponse res = getHttpClient().execute(post);
            input = res.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(res
                    .getEntity().getContent(), "UTF-8");
            return CharStreams.toString(reader);
        } finally {
            Closeables.close(input, true);
        }
    }

    /**
     * 返回执行请求的 HttpClient 对象
     *
     * @return
     */
    public abstract HttpClient getHttpClient();
}
