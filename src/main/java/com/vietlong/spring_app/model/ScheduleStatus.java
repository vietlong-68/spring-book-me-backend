package com.vietlong.spring_app.model;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ScheduleStatus {
    AVAILABLE("Có sẵn", "Slot có thể đặt lịch"),
    BOOKED("Đã đặt", "Slot đã được đặt lịch"),
    COMPLETED("Hoàn thành", "Dịch vụ đã hoàn thành"),
    CANCELLED("Đã hủy", "Slot đã bị hủy");

    private final String displayName;
    private final String description;

    ScheduleStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public static ScheduleStatus fromString(String statusString) throws AppException {
        if (statusString == null || statusString.trim().isEmpty()) {
            return AVAILABLE;
        }

        try {
            return ScheduleStatus.valueOf(statusString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VALIDATION_ERROR,
                    "Invalid schedule status: " + statusString + ". Valid statuses are: "
                            + String.join(", ", getStatusNames()));
        }
    }

    public static String[] getStatusNames() {
        ScheduleStatus[] statuses = ScheduleStatus.values();
        String[] statusNames = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statusNames[i] = statuses[i].name();
        }
        return statusNames;
    }

    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    public boolean isBooked() {
        return this == BOOKED;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean canBeBooked() {
        return this == AVAILABLE;
    }

    public boolean canBeCancelled() {
        return this == AVAILABLE || this == BOOKED;
    }
}
