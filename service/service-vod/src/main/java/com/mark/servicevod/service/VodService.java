package com.mark.servicevod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/30 11:37
 */
public interface VodService {
    String upload(MultipartFile file) throws IOException;

    void deleteVideo(String id) throws ClientException;
}
