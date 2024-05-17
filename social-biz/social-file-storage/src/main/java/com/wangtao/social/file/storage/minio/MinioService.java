package com.wangtao.social.file.storage.minio;

import com.wangtao.social.common.core.util.DateUtils;
import com.wangtao.social.common.core.util.UuidUtils;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author wangtao
 * Created at 2023-09-24
 */
public class MinioService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    private final String defaultBucket;

    public MinioService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.defaultBucket = minioProperties.getDefaultBucket();
    }

    /**
     * 根据文件名称创建一个随机名称，以img/开头
     *
     * @param filename 文件名称
     * @return 对象名称
     */
    public String createImageObjName(String filename) {
        Objects.requireNonNull(filename, "filename is required!");
        int delim = filename.lastIndexOf('.');
        String suffix = "";
        if (delim != -1) {
            suffix = filename.substring(delim);
        }
        return "img/" + DateUtils.formatShortDate(LocalDate.now()) + "/" + UuidUtils.uuid() + suffix;
    }

    public void uploadImage(InputStream obj, String contentType) {
        String objName = UuidUtils.uuid();
        uploadImage(defaultBucket, objName, obj, contentType);
    }

    public void uploadImage(String objName, InputStream obj, String contentType) {
        uploadImage(defaultBucket, objName, obj, contentType);
    }

    /**
     * 上传图片
     *
     * @param bucket 桶位
     * @param objName 对象名称
     * @param stream 上传的图片资源
     * @param contentType 图片类型, 如image/jpeg、image/gif、image/png
     */
    public void uploadImage(String bucket, String objName, InputStream stream, String contentType) {
        Objects.requireNonNull(bucket, "bucket is required!");
        Objects.requireNonNull(objName, "objName is required!");
        Objects.requireNonNull(objName, "stream is required!");
        try {
            //判断文件存储的桶是否存在
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build()
                );
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objName)
                            .stream(stream, -1, ObjectWriteArgs.MAX_PART_SIZE)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取临时访问地址
     *
     * @param objName 对象名称
     * @return 临时访问地址
     */
    public String getTmpAccessUrl(String objName) {
        return getTmpAccessUrl(defaultBucket, objName);
    }

    /**
     * 获取临时访问地址
     *
     * @param bucket 桶位
     * @param objName 对象名称
     * @return 临时访问地址
     */
    public String getTmpAccessUrl(String bucket, String objName) {
        Objects.requireNonNull(bucket, "bucket is required!");
        Objects.requireNonNull(objName, "objName is required!");
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objName)
                            .method(Method.GET)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取直接访问的地址, 需要有匿名规则权限
     *
     * @param objName 对象名称
     * @return 直接访问地址
     */
    public String getDirectAccessUrl(String objName) {
        return getDirectAccessUrl(defaultBucket, objName);
    }

    /**
     * 获取直接访问的地址, 需要有匿名规则权限
     *
     * @param bucket 桶位
     * @param objName 对象名称
     * @return 直接访问地址
     */
    public String getDirectAccessUrl(String bucket, String objName) {
        Objects.requireNonNull(bucket, "bucket is required!");
        Objects.requireNonNull(objName, "objName is required!");
        return minioProperties.getImgAccessAddr() + "/" + bucket + "/" + objName;
    }
}
