package com.vietlong.spring_app.dto.request;

import com.vietlong.spring_app.model.DayOfWeek;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateRecurringScheduleRequest {

    @NotBlank(message = "Service ID không được để trống")
    private String serviceId;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    @NotEmpty(message = "Danh sách ngày trong tuần không được để trống")
    @Size(min = 1, message = "Phải có ít nhất 1 ngày trong tuần")
    private List<DayOfWeek> daysOfWeek;

    @NotEmpty(message = "Danh sách ca làm việc không được để trống")
    @Size(min = 1, message = "Phải có ít nhất 1 ca làm việc")
    @Valid
    private List<ShiftTime> shifts;

    @NotNull(message = "Số lượng khách tối đa không được để trống")
    @Min(value = 1, message = "Số lượng khách tối đa phải >= 1")
    private Integer maxCapacity;

    private String notes;

    @Data
    public static class ShiftTime {
        @NotBlank(message = "Thời gian bắt đầu ca không được để trống")
        @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Thời gian bắt đầu phải có định dạng HH:mm")
        private String startTime;

        @NotBlank(message = "Thời gian kết thúc ca không được để trống")
        @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Thời gian kết thúc phải có định dạng HH:mm")
        private String endTime;
    }
}
