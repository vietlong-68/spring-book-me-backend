package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.response.CategoryResponse;
import com.vietlong.spring_app.dto.response.ServiceResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.service.AdminCategoryService;
import com.vietlong.spring_app.service.ServiceManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các API công khai dành cho USER
 * Bao gồm: Xem dịch vụ, danh mục công khai
 */
@RestController
@RequestMapping("/api")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;
    private final AdminCategoryService adminCategoryService;

    public ServiceController(ServiceManagementService serviceManagementService,
            AdminCategoryService adminCategoryService) {
        this.serviceManagementService = serviceManagementService;
        this.adminCategoryService = adminCategoryService;
    }

    /**
     * API lấy danh sách dịch vụ công khai (chỉ active)
     * Yêu cầu: Cần authentication
     * 
     * @param page        Số trang (bắt đầu từ 0)
     * @param size        Số lượng dịch vụ mỗi trang
     * @param sortBy      Trường để sắp xếp (mặc định: createdAt)
     * @param sortDir     Hướng sắp xếp: asc/desc (mặc định: desc)
     * @param httpRequest HttpServletRequest
     * @return Page chứa danh sách dịch vụ công khai đã phân trang
     */
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> getPublicServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            HttpServletRequest httpRequest) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ServiceResponse> services = serviceManagementService.getPublicServicesPaginated(pageable);
        return ResponseEntity
                .ok(ApiResponse.success(services, "Lấy danh sách dịch vụ công khai thành công", httpRequest));
    }

    /**
     * API lấy chi tiết dịch vụ công khai theo ID
     * Yêu cầu: Cần authentication
     * 
     * @param serviceId   ID của dịch vụ
     * @param httpRequest HttpServletRequest
     * @return Chi tiết dịch vụ công khai
     * @throws AppException nếu không tìm thấy dịch vụ hoặc dịch vụ không active
     */
    @GetMapping("/services/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getPublicServiceById(
            @PathVariable String serviceId,
            HttpServletRequest httpRequest) throws AppException {

        ServiceResponse service = serviceManagementService.getPublicServiceById(serviceId);
        return ResponseEntity
                .ok(ApiResponse.success(service, "Lấy chi tiết dịch vụ công khai thành công", httpRequest));
    }

    /**
     * API lấy danh sách danh mục công khai (chỉ active)
     * Yêu cầu: Cần authentication
     * 
     * @param httpRequest HttpServletRequest
     * @return Danh sách danh mục công khai
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getPublicCategories(HttpServletRequest httpRequest) {
        List<CategoryResponse> categories = adminCategoryService.getPublicCategories();
        return ResponseEntity
                .ok(ApiResponse.success(categories, "Lấy danh sách danh mục công khai thành công", httpRequest));
    }

    /**
     * API lọc dịch vụ theo danh mục (chỉ active)
     * Yêu cầu: Cần authentication
     * 
     * @param categoryId  ID của danh mục
     * @param page        Số trang (bắt đầu từ 0)
     * @param size        Số lượng dịch vụ mỗi trang
     * @param sortBy      Trường để sắp xếp (mặc định: createdAt)
     * @param sortDir     Hướng sắp xếp: asc/desc (mặc định: desc)
     * @param httpRequest HttpServletRequest
     * @return Page chứa danh sách dịch vụ theo danh mục đã phân trang
     * @throws AppException nếu không tìm thấy danh mục
     */
    @GetMapping("/categories/{categoryId}/services")
    public ResponseEntity<ApiResponse<Page<ServiceResponse>>> getServicesByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            HttpServletRequest httpRequest) throws AppException {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ServiceResponse> services = serviceManagementService.getPublicServicesByCategory(categoryId, pageable);
        return ResponseEntity
                .ok(ApiResponse.success(services, "Lấy danh sách dịch vụ theo danh mục thành công", httpRequest));
    }
}
