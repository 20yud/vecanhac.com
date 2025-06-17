package com.vecanhac.ddd.controller.cloudinary;

import com.vecanhac.ddd.application.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    /**
     * Upload ảnh chính thức (theo folder: coverimg, orgimg, qr...)
     */
    @PostMapping("/{type}")
    public ResponseEntity<String> uploadImage(
            @PathVariable(name = "type") String type,
            @RequestParam("file") MultipartFile file
    ) {
        String url = cloudinaryService.uploadFile(file, type);
        return ResponseEntity.ok(url);
    }

    /**
     * Upload ảnh tạm (lưu folder temp, gắn tag 'temp')
     */
    @PostMapping("/temp")
    public ResponseEntity<String> uploadTempImage(@RequestParam("file") MultipartFile file) {
        String tempUrl = cloudinaryService.uploadTempFile(file);
        return ResponseEntity.ok(tempUrl);
    }

    /**
     * Xoá ảnh tạm bằng URL
     */
    @DeleteMapping("/temp")
    public ResponseEntity<Void> deleteTempImage(@RequestParam("url") String imageUrl) {
        cloudinaryService.deleteTempFile(imageUrl);
        return ResponseEntity.noContent().build();
    }
    /**
     * Chuyển ảnh từ temp sang folder đích (coverimg, orgimg...)
     */
    @PostMapping("/move")
    public ResponseEntity<String> moveTempImage(
            @RequestParam("tempUrl") String tempUrl,
            @RequestParam("target") String targetFolder // ví dụ: coverimg, orgimg, ...
    ) {
        String newUrl = cloudinaryService.moveTempImage(tempUrl, targetFolder);
        return ResponseEntity.ok(newUrl);
    }

}