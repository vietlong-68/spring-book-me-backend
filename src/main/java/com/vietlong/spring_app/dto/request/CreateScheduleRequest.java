package com.vietlong.spring_app.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleRequest {
    @NotNull(message = "Service ID không được để trống")
    private String serviceId;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @Future(message = "Thời gian bắt đầu phải là thời gian tương lai")
    private LocalDateTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @Future(message = "Thời gian kết thúc phải là thời gian tương lai")
    private LocalDateTime endTime;

    @NotNull(message = "Sức chứa tối đa không được để trống")
    @Positive(message = "Sức chứa tối đa phải lớn hơn 0")
    private Integer maxCapacity;

    private String notes;
}
