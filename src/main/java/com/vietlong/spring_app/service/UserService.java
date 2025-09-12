package com.vietlong.spring_app.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.User;
import com.vietlong.spring_app.repository.UserRepository;
import com.vietlong.spring_app.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

    public UserResponse getCurrentUserProfile(Authentication authentication) throws AppException {
        String userId = getUserIdFromAuthentication(authentication);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy thông tin người dùng"));

        return UserResponse.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .isEmailVerified(user.getIsEmailVerified())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
