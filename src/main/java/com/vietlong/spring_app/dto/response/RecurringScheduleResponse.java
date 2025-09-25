package com.vietlong.spring_app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecurringScheduleResponse {
    private Integer createdCount;
}
