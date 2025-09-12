package com.vietlong.spring_app.service.uploadfile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryFileUploadService implements FileUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        try {
            if (file == null || file.isEmpty()) {
                throw new IOException("File không được để trống");
            }

            long maxSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxSize) {
                throw new IOException("File không được vượt quá 10MB");
            }

            String contentType = file.getContentType();
            if (contentType == null || !isValidImageType(contentType)) {
                throw new IOException("Chỉ hỗ trợ file ảnh (JPG, PNG, GIF, WebP)");
            }

            String publicId = "book-me/" + UUID.randomUUID().toString();

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", "book-me/uploads",
                    "resource_type", "auto",
                    "quality", "auto",
                    "fetch_format", "auto");

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    uploadOptions);

            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Lỗi khi upload file: " + e.getMessage());
        }
    }

    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }

}