package com.vietlong.spring_app.controller.admin;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.response.ServiceResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.service.ServiceManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các API quản lý dịch vụ dành cho Admin
 * Bao gồm: Xem tất cả dịch vụ, phân trang, chi tiết dịch vụ
 */
@RestController
@RequestMapping("/api/admin/services")
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceController {

    private final ServiceManagementService serviceManagementService;

    public AdminServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    /**
     * API lấy tất cả dịch vụ trong hệ thống
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param httpRequest HttpServletRequest
     * @return Danh sách tất cả dịch vụ
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getAllServices(HttpServletRequest httpRequest) {
        List<ServiceResponse> services = serviceManagementService.getAllServices();
        return ResponseEntity.ok(ApiResponse.success(services, "Lấy danh sách dịch vụ thành công", httpRequest));
    }

    /**
     * API lấy danh sách dịch vụ có phân trang và sắp xếp
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param page        Số trang (bắt đầu từ 0)
     * @param size        Số lượng dịch vụ mỗi trang
     * @param sortBy      Trường để sắp xếp (mặc định: createdAt)
     * @param sortDir     Hướng sắp xếp: asc/desc (mặc định: desc)
     * @param httpRequest HttpServletRequest
     * @return Page chứa danh sách dịch vụ đã phân trang
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> getAllServicesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            HttpServletRequest httpRequest) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ServiceResponse> services = serviceManagementService.getAllServicesPaginated(pageable);

        return ResponseEntity
                .ok(ApiResponse.success(services, "Lấy danh sách dịch vụ có phân trang thành công", httpRequest));
    }

    /**
     * API lấy chi tiết dịch vụ theo ID
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param serviceId   ID của dịch vụ
     * @param httpRequest HttpServletRequest
     * @return Chi tiết dịch vụ
     * @throws AppException nếu không tìm thấy dịch vụ
     */
    @GetMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(
            @PathVariable("serviceId") String serviceId,
            HttpServletRequest httpRequest) throws AppException {
        ServiceResponse service = serviceManagementService.getServiceByIdForAdmin(serviceId);
        return ResponseEntity.ok(ApiResponse.success(service, "Lấy chi tiết dịch vụ thành công", httpRequest));
    }
}
