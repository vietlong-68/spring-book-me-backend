package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.UpdateProviderRequest;
import com.vietlong.spring_app.dto.response.ProviderResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.service.ProviderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller xử lý các API liên quan đến Provider
 * Bao gồm: xem thông tin, cập nhật thông tin, upload logo/banner
 */
@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    /**
     * Lấy danh sách Provider của người dùng hiện tại
     * 
     * @param authentication Thông tin xác thực người dùng
     * @param httpRequest    Http request
     * @return Danh sách Provider của người dùng
     * @throws AppException Nếu có lỗi xảy ra
     */
    @GetMapping("/my-providers")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProviderResponse>>> getMyProviders(
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        List<ProviderResponse> response = providerService.getMyProviders(authentication);
        return ResponseEntity.ok(ApiResponse.success(response, "Lấy danh sách Provider thành công", httpRequest));
    }

    /**
     * Lấy thông tin Provider theo ID
     * 
     * @param providerId     ID của Provider
     * @param authentication Thông tin xác thực người dùng
     * @param httpRequest    Http request
     * @return Thông tin Provider
     * @throws AppException Nếu có lỗi xảy ra
     */
    @GetMapping("/{providerId}")
    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProviderResponse>> getProviderById(
            @PathVariable(name = "providerId") String providerId,
            HttpServletRequest httpRequest) throws AppException {

        ProviderResponse response = providerService.getProviderById(providerId);
        return ResponseEntity.ok(ApiResponse.success(response, "Lấy thông tin Provider thành công", httpRequest));
    }

    /**
     * Cập nhật thông tin Provider
     * 
     * @param providerId     ID của Provider
     * @param authentication Thông tin xác thực người dùng
     * @param request        Thông tin cập nhật
     * @param logoFile       File logo mới (tùy chọn)
     * @param bannerFile     File banner mới (tùy chọn)
     * @param httpRequest    Http request
     * @return Thông tin Provider đã cập nhật
     * @throws AppException Nếu có lỗi xảy ra
     */
    @PutMapping("/{providerId}")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProviderResponse>> updateProvider(
            @PathVariable(name = "providerId") String providerId,
            Authentication authentication,
            @Valid @ModelAttribute UpdateProviderRequest request,
            @RequestParam(name = "logoFile", required = false) MultipartFile logoFile,
            @RequestParam(name = "bannerFile", required = false) MultipartFile bannerFile,
            HttpServletRequest httpRequest) throws AppException {

        ProviderResponse response = providerService.updateProvider(
                providerId, authentication, request, logoFile, bannerFile);
        return ResponseEntity.ok(ApiResponse.success(response, "Cập nhật thông tin Provider thành công", httpRequest));
    }

    /**
     * Cập nhật logo Provider
     * 
     * @param providerId     ID của Provider
     * @param authentication Thông tin xác thực người dùng
     * @param logoFile       File logo mới
     * @param httpRequest    Http request
     * @return Thông tin Provider đã cập nhật logo
     * @throws AppException Nếu có lỗi xảy ra
     */
    @PutMapping("/{providerId}/logo")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProviderResponse>> updateProviderLogo(
            @PathVariable(name = "providerId") String providerId,
            Authentication authentication,
            @RequestParam(name = "logoFile") MultipartFile logoFile,
            HttpServletRequest httpRequest) throws AppException {

        if (logoFile == null || logoFile.isEmpty()) {
            throw new AppException(ErrorCode.BAD_REQUEST, "File logo không được để trống");
        }

        String contentType = logoFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.BAD_REQUEST, "File phải là định dạng ảnh");
        }

        if (logoFile.getSize() > 5 * 1024 * 1024) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Kích thước file không được vượt quá 5MB");
        }

        ProviderResponse response = providerService.updateProviderLogo(providerId, authentication, logoFile);
        return ResponseEntity.ok(ApiResponse.success(response, "Cập nhật logo Provider thành công", httpRequest));
    }

    /**
     * Cập nhật banner Provider
     * 
     * @param providerId     ID của Provider
     * @param authentication Thông tin xác thực người dùng
     * @param bannerFile     File banner mới
     * @param httpRequest    Http request
     * @return Thông tin Provider đã cập nhật banner
     * @throws AppException Nếu có lỗi xảy ra
     */
    @PutMapping("/{providerId}/banner")
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProviderResponse>> updateProviderBanner(
            @PathVariable(name = "providerId") String providerId,
            Authentication authentication,
            @RequestParam(name = "bannerFile") MultipartFile bannerFile,
            HttpServletRequest httpRequest) throws AppException {

        if (bannerFile == null || bannerFile.isEmpty()) {
            throw new AppException(ErrorCode.BAD_REQUEST, "File banner không được để trống");
        }

        String contentType = bannerFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.BAD_REQUEST, "File phải là định dạng ảnh");
        }

        if (bannerFile.getSize() > 10 * 1024 * 1024) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Kích thước file không được vượt quá 10MB");
        }

        ProviderResponse response = providerService.updateProviderBanner(providerId, authentication, bannerFile);
        return ResponseEntity.ok(ApiResponse.success(response, "Cập nhật banner Provider thành công", httpRequest));
    }

}
