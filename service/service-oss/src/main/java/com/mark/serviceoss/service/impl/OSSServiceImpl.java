package com.mark.serviceoss.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.serviceoss.service.OSSService;
import com.mark.serviceoss.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/23 11:03
 */
@Service
public class OSSServiceImpl implements OSSService {

    @Override
    public String fileUpload(MultipartFile multipartFile) {

        // 从常量类中获取配置信息
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String key = ConstantPropertiesUtil.KEY;
        String secret = ConstantPropertiesUtil.SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String filePatten = ConstantPropertiesUtil.FILE_PATTEN;

        // 构建oss对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, key, secret);

        String url;

        try {
            // 生成日期文件夹 如：2020/12/23
            String filePath = DateUtil.format(new Date(), "yyyy/MM/dd");

            // 设置随机文件名 如： uuid.jpg
            // 获取源文件名称
            String originalName = multipartFile.getOriginalFilename();
            // 获取源文件后缀名
            assert originalName != null;
            String fileType = originalName.substring(originalName.lastIndexOf("."));
            // 生成uuid
            String fileName = UUID.randomUUID().toString().replace("-", "");
            // 拼接
            filePath = filePatten + "/" + filePath + "/" + fileName + fileType;

            // 构建文件上传对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, multipartFile.getInputStream());

            // 文件上传
            ossClient.putObject(putObjectRequest);
            // 关闭资源连接
            ossClient.shutdown();

            url = "https://" + bucketName + "." + endpoint + "/" + filePath;

        } catch (IOException e) {
            throw new CustomException(CustomExceptionEnum.FILE_UPLOAD_ERROR);
        }
        return url;
    }

    @Override
    public void deleteFile(String filename) {
        // 从常量类中获取配置信息
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String key = ConstantPropertiesUtil.KEY;
        String secret = ConstantPropertiesUtil.SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, key, secret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, filename);

        ossClient.shutdown();
    }
}
