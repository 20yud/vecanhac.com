package com.vecanhac.ddd.application.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadFile(MultipartFile file, String folder);
    String uploadFile(MultipartFile file, String folder, boolean isTemp);
    String uploadTempFile(MultipartFile file);
    void deleteTempFile(String imageUrl);
    String moveTempImage(String tempUrl, String targetFolder);

}