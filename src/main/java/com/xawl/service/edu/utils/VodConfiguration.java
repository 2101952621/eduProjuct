package com.xawl.service.edu.utils;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("aliyun.vod.file")
public class VodConfiguration implements InitializingBean {

    private String keyId;
    private String keySecret;

    public static String KEY_ID;
    public static String KEY_SECRET;


    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
    }
}
