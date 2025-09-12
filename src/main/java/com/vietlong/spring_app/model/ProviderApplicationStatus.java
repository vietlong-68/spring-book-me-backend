package com.vietlong.spring_app.model;

public enum ProviderApplicationStatus {
    PENDING("Đang chờ duyệt"),
    APPROVED("Đã duyệt"),
    REJECTED("Đã từ chối"),
    CANCELLED("Đã hủy"),
    COMPLETED("Đã hoàn thành");

    private final String displayName;

    ProviderApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isApproved() {
        return this == APPROVED;
    }

    public boolean isRejected() {
        return this == REJECTED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isFinalStatus() {
        return this == APPROVED || this == REJECTED || this == CANCELLED || this == COMPLETED;
    }
}
