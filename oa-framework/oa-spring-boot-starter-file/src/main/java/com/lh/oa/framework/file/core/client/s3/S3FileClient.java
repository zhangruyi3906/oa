package com.lh.oa.framework.file.core.client.s3;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.lh.oa.framework.file.core.client.AbstractFileClient;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;

/**
 * 基于 S3 协议的文件客户端，实现 MinIO、阿里云、腾讯云、七牛云、华为云等云服务
 * <p>
 * S3 协议的客户端，采用亚马逊提供的 software.amazon.awssdk.s3 库
 *
 * @author
 */
public class S3FileClient extends AbstractFileClient<S3FileClientConfig> {

    private MinioClient client;

    public S3FileClient(Long id, S3FileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // 补全 domain
        if (StrUtil.isEmpty(config.getDomain())) {
            config.setDomain(buildDomain());
        }
        // 初始化客户端
        client = MinioClient.builder()
                .endpoint(buildEndpointURL()) // Endpoint URL
                .region(buildRegion()) // Region
                .credentials(config.getAccessKey(), config.getAccessSecret()) // 认证密钥
                .build();
    }

    /**
     * 基于 endpoint 构建调用云服务的 URL 地址
     *
     * @return URI 地址
     */
    private String buildEndpointURL() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (HttpUtil.isHttp(config.getEndpoint()) || HttpUtil.isHttps(config.getEndpoint())) {
            return config.getEndpoint();
        }
        return StrUtil.format("https://{}", config.getEndpoint());
    }

    /**
     * 基于 bucket + endpoint 构建访问的 Domain 地址
     *
     * @return Domain 地址
     */
    private String buildDomain() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (HttpUtil.isHttp(config.getEndpoint()) || HttpUtil.isHttps(config.getEndpoint())) {
            return StrUtil.format("{}/{}", config.getEndpoint(), config.getBucket());
        }
        // 阿里云、腾讯云、华为云都适合。七牛云比较特殊，必须有自定义域名
        return StrUtil.format("https://{}.{}", config.getBucket(), config.getEndpoint());
    }

    /**
     * 基于 bucket 构建 region 地区
     *
     * @return region 地区
     */
    private String buildRegion() {
        if (config.getEndpoint().contains(S3FileClientConfig.ENDPOINT_ALIYUN)) {
            return StrUtil.subBefore(config.getEndpoint(), '.', false)
                    .replaceAll("-internal", "")
                    .replaceAll("https://", "");
        }
        if (config.getEndpoint().contains(S3FileClientConfig.ENDPOINT_TENCENT)) {
            return StrUtil.subAfter(config.getEndpoint(), ".cos.", false)
                    .replaceAll("." + S3FileClientConfig.ENDPOINT_TENCENT, "");
        }
        return null;
    }

    @Override
    public String upload(byte[] content, String path, String type) throws Exception {

        client.putObject(PutObjectArgs.builder()
                .bucket(config.getBucket())
                .contentType(type)
                .object(path)
                .stream(new ByteArrayInputStream(content), content.length, -1)
                .build());

        return config.getDomain() + "/" + path;
    }

    @Override
    public void delete(String path) throws Exception {
        client.removeObject(RemoveObjectArgs.builder()
                .bucket(config.getBucket())
                .object(path)
                .build());
    }

    @Override
    public byte[] getContent(String path) throws Exception {
//        GetObjectResponse response = client.getObject(GetObjectArgs.builder()
//                .bucket(config.getBucket()) // bucket 必须传递
//                .object(path) // 相对路径作为 key
//                .build());


        OSS ossClient = new OSSClientBuilder()
                .build(config.getEndpoint(), config.getAccessKey(), config.getAccessSecret());
//        path = new URL(path).getPath();

        try {

            String bucketName = config.getBucket();

            String objectKey = path;

            OSSObject ossObject = ossClient.getObject(new GetObjectRequest(bucketName, objectKey));

            byte[] contentBytes = IOUtils.toByteArray(ossObject.getObjectContent());

            ossClient.shutdown();

            return contentBytes;
        } catch (Exception e) {
            throw e;
        }
//        return IoUtil.readBytes(response);
    }

}
