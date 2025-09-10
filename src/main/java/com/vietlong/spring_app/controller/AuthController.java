package com.vietlong.spring_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.IntrospectTokenRequest;
import com.vietlong.spring_app.dto.request.LoginRequest;
import com.vietlong.spring_app.dto.request.RegisterRequest;
import com.vietlong.spring_app.dto.response.IntrospectTokenResponse;
import com.vietlong.spring_app.dto.response.LoginResponse;
import com.vietlong.spring_app.dto.response.UserResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.service.AuthService;
import com.vietlong.spring_app.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Date;

/**
 * Controller xử lý các API liên quan đến xác thực (authentication)
 * Bao gồm: đăng ký, đăng nhập, đăng xuất, kiểm tra token
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthService authService, TokenBlacklistService tokenBlacklistService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * API đăng ký tài khoản mới
     * 
     * @param registerRequest Thông tin đăng ký (email, password, displayName, ...)
     * @param request         HttpServletRequest
     * @return UserResponse với thông tin user đã tạo
     * @throws AppException nếu email đã tồn tại hoặc validation lỗi
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request) throws AppException {
        UserResponse userResponse = authService.handleRegister(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userResponse, "Đăng ký tài khoản thành công", request));
    }

    /**
     * API đăng nhập hệ thống
     * 
     * @param loginRequest Thông tin đăng nhập (email, password)
     * @param request      HttpServletRequest
     * @return LoginResponse chứa JWT token
     * @throws AppException nếu thông tin đăng nhập không đúng
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest,
            HttpServletRequest request) throws AppException {
        LoginResponse loginResponse = authService.handleLogin(loginRequest, request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse, "Đăng nhập thành công", request));
    }

    /**
     * API kiểm tra tính hợp lệ của JWT token
     * 
     * @param introspectRequest Chứa token cần kiểm tra
     * @param request           HttpServletRequest
     * @return IntrospectTokenResponse với thông tin token (valid/invalid, claims,
     *         ...)
     */
    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectTokenResponse>> introspectToken(
            @Valid @RequestBody IntrospectTokenRequest introspectRequest,
            HttpServletRequest request) {
        IntrospectTokenResponse introspectResponse = authService.introspectToken(introspectRequest);

        String message = introspectResponse.getIsValid() ? "Token hợp lệ" : "Token không hợp lệ";

        return ResponseEntity.ok(ApiResponse.success(introspectResponse, message, request));
    }

    /**
     * API đăng xuất khỏi hệ thống
     * Yêu cầu: User phải đã đăng nhập (có JWT token hợp lệ)
     * 
     * @param authentication Thông tin xác thực của user hiện tại
     * @param request        HttpServletRequest
     * @return ApiResponse với thông báo đăng xuất thành công
     * @throws AppException nếu có lỗi trong quá trình đăng xuất
     */
    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Void>> logout(
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        try {
            String jti = authService.getJtiFromAuthentication(authentication);
            String userId = authService.getUserIdFromAuthentication(authentication);
            Date expirationTime = authService.getExpirationTimeFromAuthentication(authentication);

            tokenBlacklistService.blacklistToken(jti, userId, expirationTime, "LOGOUT");

            return ResponseEntity.ok(ApiResponse.success(null, "Đăng xuất thành công", request));

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}