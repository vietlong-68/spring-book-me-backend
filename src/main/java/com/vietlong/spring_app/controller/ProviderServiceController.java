package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.CreateServiceRequest;
import com.vietlong.spring_app.dto.request.UpdateServiceRequest;
import com.vietlong.spring_app.dto.response.ServiceResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.service.ServiceManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Controller xử lý các API quản lý dịch vụ cho Provider
 * Bao gồm: CRUD dịch vụ, kích hoạt/vô hiệu hóa dịch vụ
 */
@RestController
@RequestMapping("/api/provider/services")
public class ProviderServiceController {

    private final ServiceManagementService serviceManagementService;

    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList(
            "createdAt", "updatedAt", "serviceName", "price", "durationMinutes", "isActive");

    public ProviderServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    /**
     * API tạo dịch vụ mới
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ tạo được dịch vụ cho
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param request        Thông tin tạo dịch vụ
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return ServiceResponse với thông tin dịch vụ đã tạo
     * @throws AppException nếu có lỗi validation hoặc business logic
     */
    @PostMapping("/{providerId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(
            @PathVariable String providerId,
            @Valid @ModelAttribute CreateServiceRequest request,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        ServiceResponse serviceResponse = serviceManagementService.createService(providerId, request, imageFile,
                authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(serviceResponse, "Tạo dịch vụ thành công", httpRequest));
    }

    /**
     * API lấy danh sách dịch vụ của provider cụ thể
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ lấy được dịch vụ của
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return Danh sách dịch vụ của provider
     * @throws AppException nếu có lỗi khi lấy danh sách
     */
    @GetMapping("/{providerId}/services")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getProviderServices(
            @PathVariable String providerId,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        List<ServiceResponse> services = serviceManagementService.getProviderServices(providerId, authentication);
        return ResponseEntity.ok(ApiResponse.success(services, "Lấy danh sách dịch vụ thành công", httpRequest));
    }

    /**
     * API lấy danh sách dịch vụ của provider có phân trang
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ lấy được dịch vụ của
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param page           Số trang (bắt đầu từ 0)
     * @param size           Số lượng dịch vụ mỗi trang
     * @param sortBy         Trường để sắp xếp (mặc định: createdAt)
     * @param sortDir        Hướng sắp xếp: asc/desc (mặc định: desc)
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return Page chứa danh sách dịch vụ đã phân trang
     * @throws AppException nếu có lỗi khi lấy danh sách
     */
    @GetMapping("/{providerId}/services/paginated")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> getProviderServicesPaginated(
            @PathVariable String providerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new AppException(ErrorCode.INVALID_SORT_FIELD);
        }

        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Hướng sắp xếp phải là 'asc' hoặc 'desc'");
        }

        if (page < 0) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Số trang phải lớn hơn hoặc bằng 0");
        }
        if (size <= 0 || size > 100) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Kích thước trang phải từ 1 đến 100");
        }

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ServiceResponse> services = serviceManagementService.getProviderServicesPaginated(providerId,
                authentication,
                pageable);
        return ResponseEntity.ok(ApiResponse.success(services, "Lấy danh sách dịch vụ thành công", httpRequest));
    }

    /**
     * API lấy chi tiết dịch vụ theo ID
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ lấy được dịch vụ của
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param serviceId      ID của dịch vụ
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return ServiceResponse với thông tin chi tiết dịch vụ
     * @throws AppException nếu dịch vụ không tồn tại hoặc không thuộc về provider
     */
    @GetMapping("/{providerId}/services/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(
            @PathVariable String providerId,
            @PathVariable String serviceId,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        ServiceResponse serviceResponse = serviceManagementService.getServiceById(providerId, serviceId,
                authentication);
        return ResponseEntity.ok(ApiResponse.success(serviceResponse, "Lấy thông tin dịch vụ thành công", httpRequest));
    }

    /**
     * API cập nhật dịch vụ
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ cập nhật được dịch vụ của
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param serviceId      ID của dịch vụ
     * @param request        Thông tin cập nhật dịch vụ
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return ServiceResponse với thông tin dịch vụ đã cập nhật
     * @throws AppException nếu dịch vụ không tồn tại hoặc không thuộc về provider
     */
    @PutMapping("/{providerId}/services/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable String providerId,
            @PathVariable String serviceId,
            @Valid @ModelAttribute UpdateServiceRequest request,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        ServiceResponse serviceResponse = serviceManagementService.updateService(providerId, serviceId, request,
                imageFile,
                authentication);
        return ResponseEntity.ok(ApiResponse.success(serviceResponse, "Cập nhật dịch vụ thành công", httpRequest));
    }

    /**
     * API xóa dịch vụ
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ xóa được dịch vụ của
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param serviceId      ID của dịch vụ
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return Thông báo xóa thành công
     * @throws AppException nếu dịch vụ không tồn tại hoặc không thuộc về provider
     */
    @DeleteMapping("/{providerId}/services/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Void>> deleteService(
            @PathVariable String providerId,
            @PathVariable String serviceId,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        serviceManagementService.deleteService(providerId, serviceId, authentication);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa dịch vụ thành công", httpRequest));
    }

    /**
     * API kích hoạt dịch vụ
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ kích hoạt được dịch vụ của
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param serviceId      ID của dịch vụ
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return ServiceResponse với thông tin dịch vụ đã kích hoạt
     * @throws AppException nếu dịch vụ không tồn tại hoặc không thuộc về provider
     */
    @PutMapping("/{providerId}/services/{serviceId}/activate")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> activateService(
            @PathVariable String providerId,
            @PathVariable String serviceId,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        ServiceResponse serviceResponse = serviceManagementService.activateService(providerId, serviceId,
                authentication);
        return ResponseEntity.ok(ApiResponse.success(serviceResponse, "Kích hoạt dịch vụ thành công", httpRequest));
    }

    /**
     * API vô hiệu hóa dịch vụ
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ vô hiệu hóa được dịch vụ
     * của provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param serviceId      ID của dịch vụ
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return ServiceResponse với thông tin dịch vụ đã vô hiệu hóa
     * @throws AppException nếu dịch vụ không tồn tại hoặc không thuộc về provider
     */
    @PutMapping("/{providerId}/services/{serviceId}/deactivate")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> deactivateService(
            @PathVariable String providerId,
            @PathVariable String serviceId,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        ServiceResponse serviceResponse = serviceManagementService.deactivateService(providerId, serviceId,
                authentication);
        return ResponseEntity.ok(ApiResponse.success(serviceResponse, "Vô hiệu hóa dịch vụ thành công", httpRequest));
    }

    /**
     * API upload ảnh cho dịch vụ
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập và chỉ upload được ảnh cho dịch
     * vụ của
     * provider thuộc về user
     * 
     * @param providerId     ID của provider
     * @param serviceId      ID của dịch vụ
     * @param imageFile      File ảnh cần upload
     * @param authentication Thông tin xác thực của provider
     * @param httpRequest    HttpServletRequest
     * @return ServiceResponse với thông tin dịch vụ đã cập nhật ảnh
     * @throws AppException nếu dịch vụ không tồn tại hoặc không thuộc về provider
     */
    @PutMapping("/{providerId}/services/{serviceId}/upload-image")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceResponse>> uploadServiceImage(
            @PathVariable String providerId,
            @PathVariable String serviceId,
            @RequestParam("imageFile") MultipartFile imageFile,
            Authentication authentication,
            HttpServletRequest httpRequest) throws AppException {

        if (imageFile == null || imageFile.isEmpty()) {
            throw new AppException(ErrorCode.BAD_REQUEST, "File ảnh không được để trống");
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "File ảnh phải là định dạng ảnh (JPG, PNG, JPEG, GIF)");
        }

        if (imageFile.getSize() > 10 * 1024 * 1024) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Kích thước file không được vượt quá 10MB");
        }

        ServiceResponse serviceResponse = serviceManagementService.uploadServiceImage(providerId, serviceId, imageFile,
                authentication);
        return ResponseEntity.ok(ApiResponse.success(serviceResponse, "Upload ảnh dịch vụ thành công", httpRequest));
    }
}