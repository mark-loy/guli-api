package com.mark.servicevod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.servicevod.service.VodService;
import com.mark.servicevod.util.ConstantVodUtil;
import com.mark.servicevod.util.InitVodClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/30 11:37
 */
@Service
@Slf4j
public class VodServiceImpl implements VodService {

    @Override
    public String upload(MultipartFile file) throws IOException {
        // 获取常量值
        String accessKeyId = ConstantVodUtil.KEY;
        String accessKeySecret = ConstantVodUtil.SECRET;

        // 源文件名
        String fileName = file.getOriginalFilename();
        // 上传后显示的文件名
        assert fileName != null;
        String title = fileName.substring(0, fileName.lastIndexOf("."));

        // 获取文件流
        InputStream inputStream = file.getInputStream();

        // 上传
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        // 获取上传的视频id
        String videoId = response.getVideoId();

        //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
        // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
        if (!response.isSuccess()) {
            String message = "视频上传异常 ===》 code：" + response.getCode() + " ===》 message：" + response.getMessage();
            log.info(message);
            if (StringUtils.isEmpty(videoId)) {
                // videoId 为空
                throw new CustomException(CustomExceptionEnum.VIDEO_UPLOAD_ERROR);
            }
        }
        return videoId;
    }

    @Override
    public void deleteVideo(String id) throws ClientException {
        // 初始化client对象
        DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtil.KEY, ConstantVodUtil.SECRET);
        // 构造request
        DeleteVideoRequest request = new DeleteVideoRequest();
        // 设置视频id
        request.setVideoIds(id);
        // 执行删除
        client.getAcsResponse(request);
    }

    @Override
    public String getVideoAuth(String vid) {
        try {
            // 初始化client对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtil.KEY, ConstantVodUtil.SECRET);
            // 构造凭证request
            GetVideoPlayAuthRequest authRequest = new GetVideoPlayAuthRequest();
            // 设置request
            authRequest.setVideoId(vid);
            // 上传request
            GetVideoPlayAuthResponse acsResponse = client.getAcsResponse(authRequest);
            // 获取视频播放凭证
            return acsResponse.getPlayAuth();
        } catch (ClientException e) {
            log.error("视频初始化失败：" + e.getMessage());
            throw new CustomException(CustomExceptionEnum.VIDEO_AUTH_CODE_ERROR);
        }
    }
}
