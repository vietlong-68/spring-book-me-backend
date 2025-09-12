package com.vietlong.spring_app.config;

import com.vietlong.spring_app.service.uploadfile.FileUploadService;
import com.vietlong.spring_app.service.uploadfile.LocalFileUploadService;
import com.vietlong.spring_app.service.uploadfile.CloudinaryFileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FileUploadConfig {

    @Value("${app.file.upload.service:local}")
    private String uploadServiceType;

    @Bean
    @Primary
    public FileUploadService fileUploadService(
            LocalFileUploadService localFileUploadService,
            CloudinaryFileUploadService cloudinaryFileUploadService) {

        switch (uploadServiceType.toLowerCase()) {
            case "cloudinary":
                return cloudinaryFileUploadService;
            case "local":
            default:
                return localFileUploadService;
        }
    }
}
