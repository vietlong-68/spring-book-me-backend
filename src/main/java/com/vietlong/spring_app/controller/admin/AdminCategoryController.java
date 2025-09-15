package com.vietlong.spring_app.controller.admin;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.CreateCategoryRequest;
import com.vietlong.spring_app.dto.request.UpdateCategoryRequest;
import com.vietlong.spring_app.dto.response.CategoryResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.service.AdminCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý các API quản lý danh mục dành cho Admin
 * Bao gồm: CRUD danh mục, kích hoạt/vô hiệu hóa danh mục
 */
@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    /**
     * API tạo danh mục mới
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param request     Thông tin tạo danh mục
     * @param httpRequest HttpServletRequest
     * @return CategoryResponse với thông tin danh mục đã tạo
     * @throws AppException nếu tên danh mục đã tồn tại
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            HttpServletRequest httpRequest) throws AppException {

        CategoryResponse categoryResponse = adminCategoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(categoryResponse, "Tạo danh mục thành công", httpRequest));
    }

    /**
     * API lấy tất cả danh mục trong hệ thống
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param httpRequest HttpServletRequest
     * @return Danh sách tất cả danh mục
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(HttpServletRequest httpRequest) {
        List<CategoryResponse> categories = adminCategoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories, "Lấy danh sách danh mục thành công", httpRequest));
    }

    /**
     * API lấy chi tiết danh mục theo ID
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param categoryId  ID của danh mục
     * @param httpRequest HttpServletRequest
     * @return Chi tiết danh mục
     * @throws AppException nếu không tìm thấy danh mục
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable("id") String categoryId,
            HttpServletRequest httpRequest) throws AppException {
        CategoryResponse category = adminCategoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(ApiResponse.success(category, "Lấy chi tiết danh mục thành công", httpRequest));
    }

    /**
     * API cập nhật danh mục
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param categoryId  ID của danh mục
     * @param request     Thông tin cập nhật danh mục
     * @param httpRequest HttpServletRequest
     * @return CategoryResponse với thông tin danh mục đã cập nhật
     * @throws AppException nếu không tìm thấy danh mục hoặc tên danh mục đã tồn tại
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable("id") String categoryId,
            @Valid @RequestBody UpdateCategoryRequest request,
            HttpServletRequest httpRequest) throws AppException {
        CategoryResponse categoryResponse = adminCategoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.success(categoryResponse, "Cập nhật danh mục thành công", httpRequest));
    }

    /**
     * API xóa danh mục
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param categoryId  ID của danh mục
     * @param httpRequest HttpServletRequest
     * @throws AppException nếu không tìm thấy danh mục hoặc danh mục đang được sử
     *                      dụng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable("id") String categoryId,
            HttpServletRequest httpRequest) throws AppException {
        adminCategoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa danh mục thành công", httpRequest));
    }

    /**
     * API kích hoạt danh mục
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param categoryId  ID của danh mục
     * @param httpRequest HttpServletRequest
     * @return CategoryResponse với thông tin danh mục đã kích hoạt
     * @throws AppException nếu không tìm thấy danh mục
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CategoryResponse>> activateCategory(
            @PathVariable("id") String categoryId,
            HttpServletRequest httpRequest) throws AppException {
        CategoryResponse categoryResponse = adminCategoryService.activateCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(categoryResponse, "Kích hoạt danh mục thành công", httpRequest));
    }

    /**
     * API vô hiệu hóa danh mục
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param categoryId  ID của danh mục
     * @param httpRequest HttpServletRequest
     * @return CategoryResponse với thông tin danh mục đã vô hiệu hóa
     * @throws AppException nếu không tìm thấy danh mục
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<CategoryResponse>> deactivateCategory(
            @PathVariable("id") String categoryId,
            HttpServletRequest httpRequest) throws AppException {
        CategoryResponse categoryResponse = adminCategoryService.deactivateCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(categoryResponse, "Vô hiệu hóa danh mục thành công", httpRequest));
    }
}
