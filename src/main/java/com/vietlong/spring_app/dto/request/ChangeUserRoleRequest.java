package com.vietlong.spring_app.dto.request;

import com.vietlong.spring_app.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleRequest {

    @NotNull(message = "Role không được để trống")
    private Role role;
}
