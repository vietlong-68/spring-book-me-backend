package com.vietlong.spring_app.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;

@Service
public class UserService {

    public String getUserIdFromAuthentication(Authentication authentication) throws AppException {
        if (authentication == null) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Không tìm thấy thông tin xác thực của người dùng");
        }
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) authentication
                    .getPrincipal();
            return jwt.getClaimAsString("userId");
        }
        throw new AppException(ErrorCode.INVALID_TOKEN);
    }

}
