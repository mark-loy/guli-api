package com.mark.serviceoss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/23 11:02
 */
public interface OSSService {

    String fileUpload(MultipartFile multipartFile);

    void deleteFile(String filename);
}
