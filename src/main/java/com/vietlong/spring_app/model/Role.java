package com.vietlong.spring_app.model;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum Role {
    USER("Người dùng", "Quyền cơ bản của người dùng"),
    PROVIDER("Nhà cung cấp dịch vụ", "Quản lý lịch hẹn của mình, xác nhận/hủy appointment"),
    ADMIN("Quản trị viên", "Toàn quyền quản trị hệ thống: quản lý user, provider, service, appointment và phân quyền");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isUser() {
        return this == USER;
    }

    public static Role fromString(String roleString) throws AppException {
        if (roleString == null || roleString.trim().isEmpty()) {
            return USER;
        }

        try {
            return Role.valueOf(roleString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VALIDATION_ERROR,
                    "Invalid role: " + roleString + ". Valid roles are: " + String.join(", ", getRoleNames()));
        }
    }

    public static String[] getRoleNames() {
        Role[] roles = Role.values();
        String[] roleNames = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            roleNames[i] = roles[i].name();
        }
        return roleNames;
    }
}
