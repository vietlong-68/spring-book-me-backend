package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.CreateProviderApplicationRequest;
import com.vietlong.spring_app.dto.request.ReviewProviderApplicationRequest;
import com.vietlong.spring_app.dto.request.UpdateProviderApplicationRequest;
import com.vietlong.spring_app.dto.response.ProviderApplicationResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller xử lý các API liên quan đến đơn đăng ký Provider
 * Bao gồm: nộp đơn, xem đơn, sửa đơn, hủy đơn
 */
@RestController
@RequestMapping("/api/provider-applications")
@RequiredArgsConstructor
public class ProviderApplicationController {

        private final ProviderApplicationService providerApplicationService;

        /**
         * Nộp đơn đăng ký làm Provider
         * 
         * @param authentication      Thông tin xác thực người dùng
         * @param request             Thông tin đơn đăng ký
         * @param businessLicenseFile File giấy phép kinh doanh
         * @param httpRequest         Http request
         * @return Thông tin đơn đăng ký đã tạo
         * @throws AppException Nếu có lỗi xảy ra
         */
        @PostMapping
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<ProviderApplicationResponse>> createApplication(
                        Authentication authentication,
                        @Valid @ModelAttribute CreateProviderApplicationRequest request,
                        @RequestParam(name = "businessLicenseFile") MultipartFile businessLicenseFile,
                        HttpServletRequest httpRequest) throws AppException {

                if (businessLicenseFile == null || businessLicenseFile.isEmpty()) {
                        throw new AppException(ErrorCode.BAD_REQUEST, "File giấy phép kinh doanh không được để trống");
                }

                String contentType = businessLicenseFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                        throw new AppException(ErrorCode.BAD_REQUEST,
                                        "File giấy phép kinh doanh phải là định dạng ảnh (JPG, PNG, JPEG, GIF)");
                }

                if (businessLicenseFile.getSize() > 10 * 1024 * 1024) {
                        throw new AppException(ErrorCode.BAD_REQUEST, "Kích thước file không được vượt quá 10MB");
                }

                ProviderApplicationResponse response = providerApplicationService.createApplication(
                                authentication, request, businessLicenseFile);
                return ResponseEntity
                                .ok(ApiResponse.success(response, "Nộp đơn đăng ký Provider thành công", httpRequest));
        }

        /**
         * Lấy danh sách đơn đăng ký của người dùng hiện tại
         * 
         * @param authentication Thông tin xác thực người dùng
         * @param httpRequest    Http request
         * @return Danh sách đơn đăng ký
         * @throws AppException Nếu có lỗi xảy ra
         */
        @GetMapping("/my-applications")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<List<ProviderApplicationResponse>>> getMyApplications(
                        Authentication authentication,
                        HttpServletRequest httpRequest) throws AppException {

                List<ProviderApplicationResponse> response = providerApplicationService
                                .getUserApplications(authentication);
                return ResponseEntity
                                .ok(ApiResponse.success(response, "Lấy danh sách đơn đăng ký thành công", httpRequest));
        }

        /**
         * Lấy thông tin chi tiết đơn đăng ký theo ID
         * 
         * @param applicationId  ID của đơn đăng ký
         * @param authentication Thông tin xác thực người dùng
         * @param httpRequest    Http request
         * @return Thông tin chi tiết đơn đăng ký
         * @throws AppException Nếu có lỗi xảy ra
         */
        @GetMapping("/{applicationId}")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
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
         * Cập nhật thông tin đơn đăng ký
         * 
         * @param applicationId       ID của đơn đăng ký
         * @param authentication      Thông tin xác thực người dùng
         * @param request             Thông tin cập nhật
         * @param businessLicenseFile File giấy phép kinh doanh mới (tùy chọn)
         * @param httpRequest         Http request
         * @return Thông tin đơn đăng ký đã cập nhật
         * @throws AppException Nếu có lỗi xảy ra
         */
        @PutMapping("/{applicationId}")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<ProviderApplicationResponse>> updateApplication(
                        @PathVariable(name = "applicationId") String applicationId,
                        Authentication authentication,
                        @Valid @ModelAttribute UpdateProviderApplicationRequest request,
                        @RequestParam(name = "businessLicenseFile", required = false) MultipartFile businessLicenseFile,
                        HttpServletRequest httpRequest) throws AppException {

                ProviderApplicationResponse response = providerApplicationService.updateApplication(
                                applicationId, authentication, request, businessLicenseFile);
                return ResponseEntity.ok(ApiResponse.success(response, "Cập nhật đơn đăng ký thành công", httpRequest));
        }

        /**
         * Hủy đơn đăng ký
         * 
         * @param applicationId  ID của đơn đăng ký
         * @param authentication Thông tin xác thực người dùng
         * @param httpRequest    Http request
         * @return Kết quả hủy đơn
         * @throws AppException Nếu có lỗi xảy ra
         */
        @DeleteMapping("/{applicationId}")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<Void>> cancelApplication(
                        @PathVariable(name = "applicationId") String applicationId,
                        Authentication authentication,
                        HttpServletRequest httpRequest) throws AppException {

                providerApplicationService.cancelApplication(applicationId, authentication);
                return ResponseEntity.ok(ApiResponse.success(null, "Hủy đơn đăng ký thành công", httpRequest));
        }

        /**
         * Lấy tất cả đơn đăng ký (Admin only)
         * 
         * @param pageable    Thông tin phân trang
         * @param httpRequest Http request
         * @return Danh sách tất cả đơn đăng ký
         */
        @GetMapping("/admin/all")
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
         * Lấy đơn đăng ký theo trạng thái (Admin only)
         * 
         * @param status      Trạng thái đơn đăng ký
         * @param pageable    Thông tin phân trang
         * @param httpRequest Http request
         * @return Danh sách đơn đăng ký theo trạng thái
         */
        @GetMapping("/admin/status/{status}")
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
         * Duyệt đơn đăng ký (Admin only)
         * 
         * @param applicationId  ID của đơn đăng ký
         * @param authentication Thông tin xác thực admin
         * @param httpRequest    Http request
         * @return Thông tin đơn đăng ký đã duyệt
         * @throws AppException Nếu có lỗi xảy ra
         */
        @PutMapping("/admin/{applicationId}/approve")
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
         * Từ chối đơn đăng ký (Admin only)
         * 
         * @param applicationId  ID của đơn đăng ký
         * @param authentication Thông tin xác thực admin
         * @param request        Thông tin từ chối (lý do)
         * @param httpRequest    Http request
         * @return Thông tin đơn đăng ký đã từ chối
         * @throws AppException Nếu có lỗi xảy ra
         */
        @PutMapping("/admin/{applicationId}/reject")
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
