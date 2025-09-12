package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.service.AuthService;
import com.vietlong.spring_app.service.UserService;
import com.vietlong.spring_app.dto.response.UserResponse;
import com.vietlong.spring_app.dto.request.UpdateUserProfileRequest;
import com.vietlong.spring_app.dto.request.ChangePasswordRequest;
import com.vietlong.spring_app.dto.request.UpdateAvatarRequest;
import com.vietlong.spring_app.service.uploadfile.FileUploadService;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

/**
 * Controller xử lý các API liên quan đến thông tin user
 * Bao gồm: lấy thông tin xác thực, profile user
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    public UserController(AuthService authService, UserService userService, FileUploadService fileUploadService) {
        this.authService = authService;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }

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

    /**
     * API cập nhật thông tin cá nhân của user hiện tại
     * Yêu cầu: User phải đã đăng nhập và có role USER hoặc ADMIN hoặc PROVIDER
     * 
     * @param authentication Thông tin xác thực của user hiện tại
     * @param request        UpdateUserProfileRequest chứa thông tin cần cập nhật
     * @param request        HttpServletRequest
     * @return UserResponse chứa thông tin cá nhân đã cập nhật
     * @throws AppException nếu có lỗi khi cập nhật
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserProfileRequest request,
            HttpServletRequest httpRequest) throws AppException {

        UserResponse updatedProfile = userService.updateUserProfile(authentication, request);

        return ResponseEntity
                .ok(ApiResponse.success(updatedProfile, "Cập nhật thông tin cá nhân thành công", httpRequest));
    }

    /**
     * API thay đổi mật khẩu của user hiện tại
     * Yêu cầu: User phải đã đăng nhập và có role USER hoặc ADMIN hoặc PROVIDER
     * 
     * @param authentication Thông tin xác thực của user hiện tại
     * @param request        ChangePasswordRequest chứa mật khẩu hiện tại và mật
     *                       khẩu mới
     * @param request        HttpServletRequest
     * @return ApiResponse thông báo thành công
     * @throws AppException nếu có lỗi khi thay đổi mật khẩu
     */
    @PutMapping("/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<String>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest) throws AppException {

        userService.changePassword(authentication, request);

        return ResponseEntity.ok(ApiResponse.success("Mật khẩu đã được thay đổi thành công",
                "Thay đổi mật khẩu thành công", httpRequest));
    }

    /**
     * API cập nhật ảnh đại diện của user hiện tại
     * Yêu cầu: User phải đã đăng nhập và có role USER hoặc ADMIN hoặc PROVIDER
     * 
     * @param authentication Thông tin xác thực của user hiện tại
     * @param file           File ảnh đại diện cần upload
     * @param request        HttpServletRequest
     * @return UserResponse chứa thông tin cá nhân đã cập nhật
     * @throws AppException nếu có lỗi khi upload hoặc cập nhật
     */
    @PutMapping("/avatar")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<UserResponse>> updateAvatar(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws AppException {

        try {
            String avatarUrl = fileUploadService.uploadFile(file);

            UpdateAvatarRequest updateRequest = UpdateAvatarRequest.builder()
                    .avatarUrl(avatarUrl)
                    .build();

            UserResponse updatedProfile = userService.updateAvatar(authentication, updateRequest);

            return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Cập nhật ảnh đại diện thành công", request));

        } catch (Exception e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload ảnh đại diện: " + e.getMessage());
        }
    }
}
