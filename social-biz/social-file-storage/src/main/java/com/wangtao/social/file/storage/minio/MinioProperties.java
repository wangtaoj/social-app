package com.wangtao.social.file.storage.minio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangtao
 * Created at 2023-09-24
 */
@ConfigurationProperties(prefix = "minio")
@ToString
@Setter
@Getter
public class MinioProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String defaultBucket;

    private String imgAccessAddr;
}
