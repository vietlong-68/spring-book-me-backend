package com.vietlong.spring_app.dto.request;

import com.vietlong.spring_app.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {
    @NotBlank(message = "Tên hiển thị không được để trống")
    @Size(max = 100, message = "Tên hiển thị không được vượt quá 100 ký tự")
    private String displayName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @Size(max = 15, message = "Số điện thoại không được vượt quá 15 ký tự")
    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;
}
