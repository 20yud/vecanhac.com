package com.vecanhac.ddd.infrastructure.service.cloudinary.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vecanhac.ddd.application.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        return uploadFile(file, folder, false);
    }

    @Override
    public String uploadFile(MultipartFile file, String folder, boolean isTemp) {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "vecanhac.com/" + folder,
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false,
                    "tags", isTemp ? "temp" : null
            );
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload ảnh lên Cloudinary", e);
        }
    }

    @Override
    public String uploadTempFile(MultipartFile file) {
        return uploadFile(file, "temp", true);
    }

    @Override
    public void deleteTempFile(String imageUrl) {
        String publicId = extractPublicIdFromUrl(imageUrl);
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi xoá ảnh từ Cloudinary", e);
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            int uploadIndex = imageUrl.indexOf("/upload/");
            if (uploadIndex == -1) {
                throw new IllegalArgumentException("URL không hợp lệ");
            }
            String path = imageUrl.substring(uploadIndex + "/upload/".length());
            path = path.replaceFirst("^v\\d+/", ""); // loại bỏ version nếu có
            int dotIndex = path.lastIndexOf('.');
            return (dotIndex != -1) ? path.substring(0, dotIndex) : path;
        } catch (Exception e) {
            throw new RuntimeException("Không thể trích xuất public_id từ URL", e);
        }
    }
    @Override
    public String moveTempImage(String tempUrl, String targetFolder) {
        String publicId = extractPublicIdFromUrl(tempUrl); // e.g. "vecanhac.com/temp/abc123"
        String filename = publicId.substring(publicId.lastIndexOf('/') + 1); // abc123
        String newPublicId = "vecanhac.com/" + targetFolder + "/" + filename;

        try {
            Map<String, Object> options = ObjectUtils.asMap("overwrite", false); // không ghi đè
            Map<?, ?> result = cloudinary.uploader().rename(publicId, newPublicId, options);
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Không thể chuyển ảnh từ temp sang " + targetFolder, e);
        }
    }


}