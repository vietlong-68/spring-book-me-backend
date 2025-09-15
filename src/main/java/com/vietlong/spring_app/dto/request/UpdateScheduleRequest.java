package com.vietlong.spring_app.dto.request;

import jakarta.validation.constraints.Future;
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
public class UpdateScheduleRequest {
    @Future(message = "Thời gian bắt đầu phải là thời gian tương lai")
    private LocalDateTime startTime;

    @Future(message = "Thời gian kết thúc phải là thời gian tương lai")
    private LocalDateTime endTime;

    @Positive(message = "Sức chứa tối đa phải lớn hơn 0")
    private Integer maxCapacity;

    private String notes;
}
