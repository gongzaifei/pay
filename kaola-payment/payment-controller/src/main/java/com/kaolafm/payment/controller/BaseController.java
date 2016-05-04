package com.kaolafm.payment.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.kaolafm.payment.request.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gongzf
 * @date 2016/3/11
 */
public class BaseController {

    public static final Logger logger = LoggerFactory.getLogger(BaseController.class);


    /**
     * 构建通用参数
     *
     * @param request
     * @return
     */
    public static CommonParams buildCommonParams(HttpServletRequest request) {

        CommonParams params = new CommonParams();

        params.setInstallid(getAttrValue(request, "installid"));
        params.setUdid(getAttrValue(request, "udid"));
        params.setSessionid(getAttrValue(request, "sessionid"));
        params.setUid(getAttrValue(request, "uid"));
        params.setAppid(getAttrValue(request, "appid"));
        params.setChannel(getAttrValue(request, "channel"));
        params.setDevicetype(getAttrValue(request, "devicetype"));
        params.setVersion(getAttrValue(request, "version"));
        params.setNetwork(getAttrValue(request, "network"));
        params.setResolution(getAttrValue(request, "resolution"));
        params.setImsi(getAttrValue(request, "imsi"));
        params.setOperator(getAttrValue(request, "operator"));
        Double lat = 0.0;
        Double lng = 0.0;
        if (getAttrValue(request, "lat") != null && !"".equals(getAttrValue(request, "lat"))) {
            lat = Double.valueOf(getAttrValue(request, "lat"));
        }
        if (getAttrValue(request, "lon") != null && !"".equals(getAttrValue(request, "lon"))) {
            lng = Double.valueOf(getAttrValue(request, "lon"));
        }
        params.setLat(lat);
        params.setLng(lng);
        params.setIp(getRealIp(request));
        params.setSuppermode(StringUtils.isBlank(getAttrValue(request, "suppermode")) ? "0" : getAttrValue(request, "suppermode"));
        params.setToken(getAttrValue(request, "token"));
        return params;
    }

    /**
     * 获取节点值</p>
     * <p/>
     * 根据节点名获取对应值</p>
     * <p/>
     * 优先获取Parameter中的值，如果为NULL 则在Attribute中获取</p>
     *
     * @return 属性值
     */
    private static String getAttrValue(HttpServletRequest request, String attr) {
        return request.getParameter(attr) == null ? (String) request.getAttribute(attr) : request.getParameter(attr);
    }

    private static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
