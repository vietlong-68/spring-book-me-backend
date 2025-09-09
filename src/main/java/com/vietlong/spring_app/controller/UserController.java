package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/authentication-info")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuthenticationInfo(
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        Map<String, Object> userAuthenticationInfo = authService.getAuthenticationInfo(authentication, request);

        String message = userAuthenticationInfo.containsKey("error") ? "Lấy thông tin xác thực thất bại"
                : "Lấy thông tin xác thực thành công";

        return ResponseEntity.ok(ApiResponse.success(userAuthenticationInfo, message, request));
    }
}
