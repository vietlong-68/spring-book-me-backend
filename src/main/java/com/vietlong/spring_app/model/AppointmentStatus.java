package com.vietlong.spring_app.model;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AppointmentStatus {
    PENDING("Chờ xác nhận", "Lịch hẹn đang chờ provider xác nhận"),
    CONFIRMED("Đã xác nhận", "Lịch hẹn đã được provider xác nhận"),
    IN_PROGRESS("Đang thực hiện", "Dịch vụ đang được thực hiện"),
    COMPLETED("Hoàn thành", "Dịch vụ đã hoàn thành"),
    CANCELLED("Đã hủy", "Lịch hẹn đã bị hủy"),
    NO_SHOW("Không đến", "Khách hàng không đến đúng giờ hẹn");

    private final String displayName;
    private final String description;

    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public static AppointmentStatus fromString(String statusString) throws AppException {
        if (statusString == null || statusString.trim().isEmpty()) {
            return PENDING;
        }

        try {
            return AppointmentStatus.valueOf(statusString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VALIDATION_ERROR,
                    "Invalid appointment status: " + statusString + ". Valid statuses are: "
                            + String.join(", ", getStatusNames()));
        }
    }

    public static String[] getStatusNames() {
        AppointmentStatus[] statuses = AppointmentStatus.values();
        String[] statusNames = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statusNames[i] = statuses[i].name();
        }
        return statusNames;
    }

    public boolean isActive() {
        return this == PENDING || this == CONFIRMED || this == IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isCancelled() {
        return this == CANCELLED || this == NO_SHOW;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean canBeConfirmed() {
        return this == PENDING;
    }

    public boolean canBeCompleted() {
        return this == CONFIRMED || this == IN_PROGRESS;
    }
}
