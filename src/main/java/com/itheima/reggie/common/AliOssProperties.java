package com.itheima.reggie.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reggie.alioss")
@Data
public class AliOssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
