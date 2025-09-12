package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.service.AuthService;
import com.vietlong.spring_app.service.UserService;
import com.vietlong.spring_app.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller xử lý các API liên quan đến thông tin user
 * Bao gồm: lấy thông tin xác thực, profile user
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * API lấy thông tin xác thực của user hiện tại
     * Yêu cầu: User phải đã đăng nhập và có role USER hoặc ADMIN hoặc PROVIDER
     * 
     * @param authentication Thông tin xác thực của user hiện tại
     * @param request        HttpServletRequest
     * @return Map chứa thông tin user, token info, permissions
     * @throws AppException nếu có lỗi khi lấy thông tin
     */
    @GetMapping("/authentication-info")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuthenticationInfo(
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        Map<String, Object> userAuthenticationInfo = authService.getAuthenticationInfo(authentication, request);

        String message = userAuthenticationInfo.containsKey("error") ? "Lấy thông tin xác thực thất bại"
                : "Lấy thông tin xác thực thành công";

        return ResponseEntity.ok(ApiResponse.success(userAuthenticationInfo, message, request));
    }

    /**
     * API lấy thông tin cá nhân của user hiện tại
     * Yêu cầu: User phải đã đăng nhập và có role USER hoặc ADMIN hoặc PROVIDER
     * 
     * @param authentication Thông tin xác thực của user hiện tại
     * @param request        HttpServletRequest
     * @return UserResponse chứa thông tin cá nhân đầy đủ
     * @throws AppException nếu có lỗi khi lấy thông tin
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserProfile(
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        UserResponse userProfile = userService.getCurrentUserProfile(authentication);

        return ResponseEntity.ok(ApiResponse.success(userProfile, "Lấy thông tin cá nhân thành công", request));
    }

    /**
     * API lấy thông tin cá nhân của một người dùng bất kỳ theo ID
     * Yêu cầu: User phải đã đăng nhập và có role ADMIN
     * 
     * @param userId  ID của người dùng cần lấy thông tin
     * @param request HttpServletRequest
     * @return UserResponse chứa thông tin cá nhân đầy đủ
     * @throws AppException nếu có lỗi khi lấy thông tin
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfileById(
            @PathVariable String userId,
            HttpServletRequest request) throws AppException {

        UserResponse userProfile = userService.getUserProfileById(userId);

        return ResponseEntity.ok(ApiResponse.success(userProfile, "Lấy thông tin người dùng thành công", request));
    }
}
