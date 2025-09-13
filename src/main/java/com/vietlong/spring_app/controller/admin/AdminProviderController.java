package com.vietlong.spring_app.controller.admin;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.response.ProviderResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.model.ProviderStatus;
import com.vietlong.spring_app.service.ProviderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các API quản lý Provider dành cho Admin
 * Bao gồm: xem tất cả Provider, xem Provider theo trạng thái
 */
@RestController
@RequestMapping("/api/admin/providers")
@RequiredArgsConstructor
public class AdminProviderController {

    private final ProviderService providerService;

    /**
     * Lấy tất cả Provider
     * 
     * @param httpRequest Http request
     * @return Danh sách tất cả Provider
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProviderResponse>>> getAllProviders(
            HttpServletRequest httpRequest) {

        List<ProviderResponse> response = providerService.getAllProviders();
        return ResponseEntity
                .ok(ApiResponse.success(response, "Lấy danh sách tất cả Provider thành công", httpRequest));
    }

    /**
     * Lấy Provider theo trạng thái
     * 
     * @param status      Trạng thái Provider (ACTIVE, SUSPENDED)
     * @param httpRequest Http request
     * @return Danh sách Provider theo trạng thái
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProviderResponse>>> getProvidersByStatus(
            @PathVariable(name = "status") ProviderStatus status,
            HttpServletRequest httpRequest) {

        List<ProviderResponse> response = providerService.getProvidersByStatus(status);
        return ResponseEntity
                .ok(ApiResponse.success(response, "Lấy danh sách Provider theo trạng thái thành công", httpRequest));
    }

    /**
     * Thay đổi trạng thái Provider (chỉ Admin)
     * 
     * @param providerId  ID của Provider cần thay đổi trạng thái
     * @param status      Trạng thái mới (ACTIVE, SUSPENDED)
     * @param httpRequest Http request
     * @return Thông tin Provider đã cập nhật trạng thái
     * @throws AppException Nếu có lỗi xảy ra
     */
    @PutMapping("/{providerId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProviderResponse>> updateProviderStatus(
            @PathVariable(name = "providerId") String providerId,
            @RequestParam(name = "status") ProviderStatus status,
            HttpServletRequest httpRequest) throws AppException {

        ProviderResponse response = providerService.updateProviderStatus(providerId, status);
        return ResponseEntity.ok(ApiResponse.success(response, "Cập nhật trạng thái Provider thành công", httpRequest));
    }
}
