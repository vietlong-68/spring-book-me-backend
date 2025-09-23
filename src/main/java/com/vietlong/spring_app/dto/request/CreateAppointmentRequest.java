package com.vietlong.spring_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {
    @NotNull(message = "Schedule ID không được để trống")
    private String scheduleId;

    private String notesFromUser;
}
