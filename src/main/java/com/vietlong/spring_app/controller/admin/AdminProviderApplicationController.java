package com.vietlong.spring_app.controller.admin;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.ReviewProviderApplicationRequest;
import com.vietlong.spring_app.dto.response.ProviderApplicationResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.model.ProviderApplicationStatus;
import com.vietlong.spring_app.service.ProviderApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các API quản lý đơn đăng ký Provider dành cho Admin
 * Bao gồm: xem tất cả đơn, xem đơn theo trạng thái, duyệt/từ chối đơn
 */
@RestController
@RequestMapping("/api/admin/provider-applications")
@RequiredArgsConstructor
public class AdminProviderApplicationController {

        private final ProviderApplicationService providerApplicationService;

        /**
         * Lấy tất cả đơn đăng ký Provider
         * 
         * @param pageable    Thông tin phân trang
         * @param httpRequest Http request
         * @return Danh sách tất cả đơn đăng ký
         */
        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Page<ProviderApplicationResponse>>> getAllApplications(
                        @PageableDefault(size = 10) Pageable pageable,
                        HttpServletRequest httpRequest) {

                Page<ProviderApplicationResponse> response = providerApplicationService.getAllApplications(pageable);
                return ResponseEntity
                                .ok(ApiResponse.success(response, "Lấy danh sách tất cả đơn đăng ký thành công",
                                                httpRequest));
        }

        /**
         * Lấy đơn đăng ký theo trạng thái
         * 
         * @param status      Trạng thái đơn đăng ký (PENDING, APPROVED, REJECTED, etc.)
         * @param pageable    Thông tin phân trang
         * @param httpRequest Http request
         * @return Danh sách đơn đăng ký theo trạng thái
         */
        @GetMapping("/status/{status}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<Page<ProviderApplicationResponse>>> getApplicationsByStatus(
                        @PathVariable(name = "status") ProviderApplicationStatus status,
                        @PageableDefault(size = 10) Pageable pageable,
                        HttpServletRequest httpRequest) {

                Page<ProviderApplicationResponse> response = providerApplicationService.getApplicationsByStatus(status,
                                pageable);
                return ResponseEntity
                                .ok(ApiResponse.success(response,
                                                "Lấy danh sách đơn đăng ký theo trạng thái thành công", httpRequest));
        }

        /**
         * Lấy thông tin chi tiết đơn đăng ký theo ID
         * 
         * @param applicationId  ID của đơn đăng ký
         * @param authentication Thông tin xác thực admin
         * @param httpRequest    Http request
         * @return Thông tin chi tiết đơn đăng ký
         * @throws AppException Nếu có lỗi xảy ra
         */
        @GetMapping("/{applicationId}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<ProviderApplicationResponse>> getApplicationById(
                        @PathVariable(name = "applicationId") String applicationId,
                        Authentication authentication,
                        HttpServletRequest httpRequest) throws AppException {

                ProviderApplicationResponse response = providerApplicationService.getApplicationById(applicationId,
                                authentication);
                return ResponseEntity
                                .ok(ApiResponse.success(response, "Lấy thông tin đơn đăng ký thành công", httpRequest));
        }

        /**
         * Duyệt đơn đăng ký Provider
         * Tự động tạo Provider entity và chuyển role User thành PROVIDER
         * 
         * @param applicationId  ID của đơn đăng ký
         * @param authentication Thông tin xác thực admin
         * @param httpRequest    Http request
         * @return Thông tin đơn đăng ký đã duyệt
         * @throws AppException Nếu có lỗi xảy ra
         */
        @PutMapping("/{applicationId}/approve")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<ProviderApplicationResponse>> approveApplication(
                        @PathVariable(name = "applicationId") String applicationId,
                        Authentication authentication,
                        HttpServletRequest httpRequest) throws AppException {

                ProviderApplicationResponse response = providerApplicationService.approveApplication(applicationId,
                                authentication);
                return ResponseEntity.ok(ApiResponse.success(response, "Duyệt đơn đăng ký thành công", httpRequest));
        }

        /**
         * Từ chối đơn đăng ký Provider
         * Gửi email thông báo với lý do từ chối
         * 
         * @param applicationId  ID của đơn đăng ký
         * @param authentication Thông tin xác thực admin
         * @param request        Thông tin từ chối (lý do)
         * @param httpRequest    Http request
         * @return Thông tin đơn đăng ký đã từ chối
         * @throws AppException Nếu có lỗi xảy ra
         */
        @PutMapping("/{applicationId}/reject")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<ProviderApplicationResponse>> rejectApplication(
                        @PathVariable(name = "applicationId") String applicationId,
                        Authentication authentication,
                        @Valid @RequestBody ReviewProviderApplicationRequest request,
                        HttpServletRequest httpRequest) throws AppException {

                ProviderApplicationResponse response = providerApplicationService.rejectApplication(
                                applicationId, authentication, request);
                return ResponseEntity.ok(ApiResponse.success(response, "Từ chối đơn đăng ký thành công", httpRequest));
        }
}
