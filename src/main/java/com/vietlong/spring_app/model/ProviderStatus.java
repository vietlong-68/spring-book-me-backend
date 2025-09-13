package com.vietlong.spring_app.model;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ProviderStatus {
    ACTIVE("Hoạt động", "Nhà cung cấp đang hoạt động bình thường"),
    SUSPENDED("Tạm khóa", "Nhà cung cấp bị tạm khóa do vi phạm");

    private final String displayName;
    private final String description;

    ProviderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public static ProviderStatus fromString(String statusString) throws AppException {
        if (statusString == null || statusString.trim().isEmpty()) {
            return ACTIVE;
        }

        try {
            return ProviderStatus.valueOf(statusString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VALIDATION_ERROR,
                    "Invalid provider status: " + statusString + ". Valid statuses are: "
                            + String.join(", ", getStatusNames()));
        }
    }

    public static String[] getStatusNames() {
        ProviderStatus[] statuses = ProviderStatus.values();
        String[] statusNames = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statusNames[i] = statuses[i].name();
        }
        return statusNames;
    }
}
