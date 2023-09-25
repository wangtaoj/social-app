package com.wangtao.social.file.storage.config;

import com.wangtao.social.file.storage.minio.MinioProperties;
import com.wangtao.social.file.storage.minio.MinioService;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangtao
 * Created at 2023-09-24
 */
@EnableConfigurationProperties({MinioProperties.class})
@Configuration(proxyBeanMethods = false)
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    @Bean
    public MinioService minioService(MinioClient minioClient, MinioProperties minioProperties) {
        return new MinioService(minioClient, minioProperties);
    }
}
