package com.vietlong.spring_app.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.ChangeUserRoleRequest;
import com.vietlong.spring_app.dto.request.CreateUserRequest;
import com.vietlong.spring_app.dto.request.UpdateUserRequest;
import com.vietlong.spring_app.dto.response.UserResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.service.AdminUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller xử lý các API quản lý user dành cho Admin
 * Bao gồm: CRUD user, thay đổi role, phân trang, tìm kiếm
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * API lấy danh sách tất cả user trong hệ thống
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param httpRequest HttpServletRequest
     * @return Danh sách tất cả user
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(HttpServletRequest httpRequest) {
        List<UserResponse> users = adminUserService.getAllUsers();
        ApiResponse<List<UserResponse>> response = ApiResponse.success(users, "lấy danh sách người dùng thành công",
                httpRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * API lấy danh sách user có phân trang và sắp xếp
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param page        Số trang (bắt đầu từ 0)
     * @param size        Số lượng user mỗi trang
     * @param sortBy      Trường để sắp xếp (mặc định: createdAt)
     * @param sortDir     Hướng sắp xếp: asc/desc (mặc định: desc)
     * @param httpRequest HttpServletRequest
     * @return Page chứa danh sách user đã phân trang
     */
    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            HttpServletRequest httpRequest) {
        Page<UserResponse> users = adminUserService.getAllUsersPaginated(page, size, sortBy, sortDir);
        ApiResponse<Page<UserResponse>> response = ApiResponse.success(users, "lấy danh sách người dùng thành công",
                httpRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * API lấy thông tin chi tiết của một user theo ID
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param userId      ID của user cần lấy thông tin
     * @param httpRequest HttpServletRequest
     * @return Thông tin chi tiết của user
     * @throws AppException nếu user không tồn tại
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable(name = "userId") String userId,
            HttpServletRequest httpRequest) throws AppException {
        UserResponse user = adminUserService.getUserById(userId);
        ApiResponse<UserResponse> response = ApiResponse.success(user, "lấy thông tin người dùng thành công",
                httpRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * API tạo user mới
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param request     Thông tin user cần tạo
     * @param httpRequest HttpServletRequest
     * @return Thông tin user đã được tạo
     * @throws AppException nếu email đã tồn tại hoặc validation lỗi
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request,
            HttpServletRequest httpRequest) throws AppException {
        UserResponse user = adminUserService.createUser(request);
        ApiResponse<UserResponse> response = ApiResponse.success(user, "tạo người dùng thành công", httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * API cập nhật thông tin user
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param userId      ID của user cần cập nhật
     * @param request     Thông tin cần cập nhật (chỉ cập nhật các field có giá trị)
     * @param httpRequest HttpServletRequest
     * @return Thông tin user đã được cập nhật
     * @throws AppException nếu user không tồn tại hoặc validation lỗi
     */
    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable(name = "userId") String userId,
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) throws AppException {
        UserResponse user = adminUserService.updateUser(userId, request);
        ApiResponse<UserResponse> response = ApiResponse.success(user, "cập nhật người dùng thành công", httpRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * API thay đổi role của user
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * Lưu ý: Admin không thể thay đổi role của chính mình
     * 
     * @param userId      ID của user cần thay đổi role
     * @param request     Chứa role mới (USER, PROVIDER, ADMIN)
     * @param httpRequest HttpServletRequest
     * @return Thông tin user đã được cập nhật role
     * @throws AppException nếu user không tồn tại, admin cố thay đổi role của chính
     *                      mình
     */
    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> changeUserRole(
            @PathVariable(name = "userId") String userId,
            @Valid @RequestBody ChangeUserRoleRequest request,
            HttpServletRequest httpRequest) throws AppException {
        UserResponse user = adminUserService.changeUserRole(userId, request);
        ApiResponse<UserResponse> response = ApiResponse.success(user, "thay đổi role người dùng thành công",
                httpRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * API xóa user khỏi hệ thống
     * Yêu cầu: Chỉ ADMIN mới có quyền truy cập
     * 
     * @param userId      ID của user cần xóa
     * @param httpRequest HttpServletRequest
     * @return ApiResponse với thông báo xóa thành công
     * @throws AppException nếu user không tồn tại
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable(name = "userId") String userId,
            HttpServletRequest httpRequest) throws AppException {
        adminUserService.deleteUser(userId);
        ApiResponse<Void> response = ApiResponse.success(null, "xóa người dùng thành công", httpRequest);
        return ResponseEntity.ok(response);
    }
}
