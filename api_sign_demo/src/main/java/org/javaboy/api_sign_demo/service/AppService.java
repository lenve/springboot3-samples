package org.javaboy.api_sign_demo.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@Service
public class AppService {

    private static final Map<String, String> APP_INFO = Map.of("app1", "sign1", "app2", "sign2");

    public String getAppKey(String appId) {
        return APP_INFO.get(appId);
    }
}
