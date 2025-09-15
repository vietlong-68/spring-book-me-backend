package com.vietlong.spring_app.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ServiceResponse {
    private String id;
    private String serviceName;
    private String description;
    private String imageUrl;
    private Integer durationMinutes;
    private BigDecimal price;
    private Integer bufferTimeAfter;
    private Integer capacity;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String providerId;
    private String providerBusinessName;

    private String categoryId;
    private String categoryName;

    private Integer totalDurationMinutes;
    private Boolean isGroupService;
    private Boolean isValidForBooking;
}
