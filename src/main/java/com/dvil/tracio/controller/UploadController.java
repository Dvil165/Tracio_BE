package com.dvil.tracio.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/images")
    public ResponseEntity<Map<String, Object>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();
        List<String> imageUrls = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();

        // Kiểm tra nếu không có file nào được gửi lên
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "No files uploaded"));
        }

        try {
            // Đảm bảo thư mục tồn tại
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            for (MultipartFile file : files) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR + fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    imageUrls.add("http://localhost:8080/uploads/" + fileName);
                } catch (Exception e) {
                    failedFiles.add(file.getOriginalFilename());
                }
            }

            response.put("uploaded", imageUrls);
            if (!failedFiles.isEmpty()) {
                response.put("failed", failedFiles);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "File upload failed due to server error"));
        }
    }

}


