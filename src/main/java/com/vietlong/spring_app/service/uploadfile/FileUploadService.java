package com.vietlong.spring_app.service.uploadfile;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileUploadService {

    String uploadFile(MultipartFile file) throws IOException;
}