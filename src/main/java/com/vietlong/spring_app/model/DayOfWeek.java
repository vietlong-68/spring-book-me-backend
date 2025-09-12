package com.vietlong.spring_app.model;

import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum DayOfWeek {
    MONDAY(1, "Thứ Hai"),
    TUESDAY(2, "Thứ Ba"),
    WEDNESDAY(3, "Thứ Tư"),
    THURSDAY(4, "Thứ Năm"),
    FRIDAY(5, "Thứ Sáu"),
    SATURDAY(6, "Thứ Bảy"),
    SUNDAY(7, "Chủ Nhật");

    private final int value;
    private final String displayName;

    DayOfWeek(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public static DayOfWeek fromValue(int value) throws AppException {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.value == value) {
                return day;
            }
        }
        throw new AppException(ErrorCode.VALIDATION_ERROR,
                "Invalid day of week value: " + value + ". Valid values are: 1-7");
    }

    public static DayOfWeek fromString(String dayString) throws AppException {
        if (dayString == null || dayString.trim().isEmpty()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Day of week cannot be null or empty");
        }

        try {
            return DayOfWeek.valueOf(dayString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VALIDATION_ERROR,
                    "Invalid day of week: " + dayString + ". Valid days are: " + String.join(", ", getDayNames()));
        }
    }

    public static String[] getDayNames() {
        DayOfWeek[] days = DayOfWeek.values();
        String[] dayNames = new String[days.length];
        for (int i = 0; i < days.length; i++) {
            dayNames[i] = days[i].name();
        }
        return dayNames;
    }

    public boolean isWeekday() {
        return this.value >= 1 && this.value <= 5;
    }

    public boolean isWeekend() {
        return this.value == 6 || this.value == 7;
    }
}
