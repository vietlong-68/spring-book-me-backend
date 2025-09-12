package com.vietlong.spring_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAvatarRequest {
    @NotBlank(message = "URL ảnh đại diện không được để trống")
    @Size(max = 500, message = "URL ảnh đại diện không được vượt quá 500 ký tự")
    private String avatarUrl;
}
