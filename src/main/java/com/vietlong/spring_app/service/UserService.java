package com.vietlong.spring_app.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.User;
import com.vietlong.spring_app.repository.UserRepository;
import com.vietlong.spring_app.dto.response.UserResponse;
import com.vietlong.spring_app.dto.request.UpdateUserProfileRequest;
import com.vietlong.spring_app.dto.request.ChangePasswordRequest;
import com.vietlong.spring_app.dto.request.UpdateAvatarRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public UserResponse getUserProfileById(String userId) throws AppException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND,
                        "Không tìm thấy người dùng với ID: " + userId));

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

    public UserResponse updateUserProfile(Authentication authentication, UpdateUserProfileRequest request)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy thông tin người dùng"));

        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED, "Email đã được sử dụng bởi tài khoản khác");
            }
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            if (!request.getPhoneNumber().equals(user.getPhoneNumber()) &&
                    userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new AppException(ErrorCode.PHONE_EXISTED, "Số điện thoại đã được sử dụng bởi tài khoản khác");
            }
        }

        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());

        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .displayName(updatedUser.getDisplayName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .phoneNumber(updatedUser.getPhoneNumber())
                .avatarUrl(updatedUser.getAvatarUrl())
                .dateOfBirth(updatedUser.getDateOfBirth())
                .gender(updatedUser.getGender())
                .isEmailVerified(updatedUser.getIsEmailVerified())
                .address(updatedUser.getAddress())
                .createdAt(updatedUser.getCreatedAt())
                .updatedAt(updatedUser.getUpdatedAt())
                .build();
    }

    public void changePassword(Authentication authentication, ChangePasswordRequest request) throws AppException {
        String userId = getUserIdFromAuthentication(authentication);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy thông tin người dùng"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "Mật khẩu hiện tại không đúng");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH, "Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public UserResponse updateAvatar(Authentication authentication, UpdateAvatarRequest request) throws AppException {
        String userId = getUserIdFromAuthentication(authentication);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy thông tin người dùng"));

        user.setAvatarUrl(request.getAvatarUrl());
        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .displayName(updatedUser.getDisplayName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .phoneNumber(updatedUser.getPhoneNumber())
                .avatarUrl(updatedUser.getAvatarUrl())
                .dateOfBirth(updatedUser.getDateOfBirth())
                .gender(updatedUser.getGender())
                .isEmailVerified(updatedUser.getIsEmailVerified())
                .address(updatedUser.getAddress())
                .createdAt(updatedUser.getCreatedAt())
                .updatedAt(updatedUser.getUpdatedAt())
                .build();
    }

}
