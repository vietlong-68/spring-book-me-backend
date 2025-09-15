package com.vietlong.spring_app.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateServiceRequest {

    @Size(min = 2, max = 255, message = "Tên dịch vụ phải có từ 2 đến 255 ký tự")
    private String serviceName;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

    @Size(max = 500, message = "URL hình ảnh không được vượt quá 500 ký tự")
    private String imageUrl;

    @Min(value = 1, message = "Thời gian dịch vụ phải lớn hơn 0 phút")
    @Max(value = 1440, message = "Thời gian dịch vụ không được vượt quá 24 giờ")
    private Integer durationMinutes;

    @DecimalMin(value = "0.01", message = "Giá dịch vụ phải lớn hơn 0")
    @DecimalMax(value = "99999999.99", message = "Giá dịch vụ không được vượt quá 99,999,999.99")
    private BigDecimal price;

    @Min(value = 0, message = "Thời gian buffer sau không được âm")
    @Max(value = 120, message = "Thời gian buffer sau không được vượt quá 120 phút")
    private Integer bufferTimeAfter;

    @Min(value = 1, message = "Sức chứa phải lớn hơn 0")
    @Max(value = 100, message = "Sức chứa không được vượt quá 100")
    private Integer capacity;

    private String categoryId;
}
