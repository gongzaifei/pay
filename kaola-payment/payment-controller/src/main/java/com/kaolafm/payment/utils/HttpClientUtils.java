package com.kaolafm.payment.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtils
{
    private String url;
    private String encoding = "utf-8";
    private Map<String, String> para;
    private int outTime = 180000;
    private int reqTime = 150000;
    private HttpClient httpClient;
    private Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public HttpClientUtils() {}

    public HttpClientUtils(String url)
    {
        this.url = url;
    }

    public HttpClientUtils(String url, String encoding, Map<String, String> para)
    {
        this.url = url;
        this.encoding = encoding;
        this.para = para;
    }

    private void init()
    {
        if (null == this.httpClient)
        {
            this.httpClient = new HttpClient();
            this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(this.outTime);
        }
    }

    private void init(int outTime)
    {
        if (null == this.httpClient)
        {
            this.httpClient = new HttpClient();
            this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(outTime);
        }
    }

    public String httpClientRequest(int outTime, int reqTime)
    {
        if (outTime > 0) {
            init(outTime);
        } else {
            init();
        }
        String value = null;
        if (null == this.para) {
            return value;
        }
        PostMethod postMethod = new PostMethod(this.url);
        if (reqTime > 0) {
            postMethod.getParams().setParameter("http.socket.timeout", Integer.valueOf(reqTime));
        } else {
            postMethod.getParams().setParameter("http.socket.timeout", Integer.valueOf(this.reqTime));
        }
        int len = this.para.size();
        NameValuePair[] NameValuePairs = new NameValuePair[len];
        int i = 0;
        for (String key : this.para.keySet()) {
            if (!StringUtils.isEmpty((String)this.para.get(key)))
            {
                NameValuePairs[i] = new NameValuePair(key, ((String)this.para.get(key)).toString());
                i++;
            }
        }
        postMethod.setRequestBody(NameValuePairs);
        postMethod.getParams().setParameter("http.protocol.content-charset", this.encoding);
        try
        {
            this.httpClient.executeMethod(postMethod);
            int state = postMethod.getStatusCode();
            if (state == 200) {
                value = readInputStream(postMethod.getResponseBodyAsStream());
            } else {
                this.logger.error("向(" + getUrl() + ")发请求返回state=" + state + "");
            }
        }
        catch (Exception e)
        {
            this.logger.error("发送(" + this.para + ")失败，原因(" + e.getMessage() + ")");
        }
        postMethod.releaseConnection();
        return value;
    }

    public String httpClientRequest()
    {
        return httpClientRequest(-1, -1);
    }

    public Map<String, String> httpClientRequestD()
    {
        Map<String, String> map = new HashMap();
        map.put("status", "998");
        this.logger.info("post:" + this.url);
        init();
        String value = null;
        if (null == this.para) {
            return map;
        }
        PostMethod postMethod = new PostMethod(this.url);
        postMethod.getParams().setParameter("http.socket.timeout", Integer.valueOf(this.reqTime));
        int len = this.para.size();
        NameValuePair[] NameValuePairs = new NameValuePair[len];
        int i = 0;
        for (String key : this.para.keySet()) {
            if (!StringUtils.isEmpty((String)this.para.get(key)))
            {
                NameValuePairs[i] = new NameValuePair(key, ((String)this.para.get(key)).toString());
                i++;
            }
        }
        postMethod.setRequestBody(NameValuePairs);
        postMethod.getParams().setParameter("http.protocol.content-charset", this.encoding);
        try
        {
            this.httpClient.executeMethod(postMethod);
            int state = postMethod.getStatusCode();
            map.put("status", state + "");
            if (state == 200) {
                try
                {
                    value = readInputStream(postMethod.getResponseBodyAsStream());
                    map.put("value", value);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else {
                this.logger.error("向(" + getUrl() + ")发请求返回state=" + state + "");
            }
        }
        catch (IOException e)
        {
            map.put("status", "999");
            e.printStackTrace();
        }
        postMethod.releaseConnection();
        return map;
    }

    @Deprecated
    public String httpClientGet()
    {
        return httpClientGet(-1, -1);
    }

    public String httpClientGet(int outTime, int reqTime)
    {
        this.logger.info("get:" + this.url);
        if (outTime > 0) {
            init(outTime);
        } else {
            init();
        }
        HttpMethod httpMethod = new GetMethod(this.url);
        if (reqTime > 0) {
            httpMethod.getParams().setParameter("http.socket.timeout", Integer.valueOf(reqTime));
        } else {
            httpMethod.getParams().setParameter("http.socket.timeout", Integer.valueOf(this.reqTime));
        }
        String value = null;
        try
        {
            this.httpClient.executeMethod(httpMethod);
            int state = httpMethod.getStatusCode();
            if (state == 200) {
                value = readInputStream(httpMethod.getResponseBodyAsStream());
            } else {
                this.logger.error("向(" + getUrl() + ")发请求返回state=" + state + "");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        httpMethod.releaseConnection();
        return value;
    }

    private String readInputStream(InputStream inputStream)
            throws Exception
    {
        String reTemp = "";
        String temp = null;
        boolean stop = true;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, this.encoding));
        while (stop)
        {
            temp = buffer.readLine();
            if (null == temp) {
                stop = false;
            } else {
                reTemp = reTemp + temp;
            }
        }
        return reTemp;
    }

    public String getUrl()
    {
        return this.url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Map<String, String> getPara()
    {
        return this.para;
    }

    public void setPara(Map<String, String> para)
    {
        this.para = para;
    }

    public String getEncoding()
    {
        return this.encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public int getOutTime()
    {
        return this.outTime;
    }

    public void setOutTime(int outTime)
    {
        this.outTime = outTime;
    }

    public int getReqTime()
    {
        return this.reqTime;
    }

    public void setReqTime(int reqTime)
    {
        this.reqTime = reqTime;
    }

    public HttpClient getHttpClient()
    {
        return this.httpClient;
    }

    public void setHttpClient(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }
    
    public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Content-Type", "text/xml");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}

