package com.vietlong.spring_app.dto.response;

import com.vietlong.spring_app.model.ProviderApplication;
import com.vietlong.spring_app.model.ProviderApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderApplicationResponse {

    private String id;
    private String userId;
    private String userDisplayName;
    private String userEmail;
    private String businessName;
    private String bio;
    private String address;
    private String phoneNumber;
    private String website;
    private String businessLicenseFileUrl;
    private ProviderApplicationStatus status;
    private String statusDisplayName;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String reviewedByDisplayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProviderApplicationResponse fromEntity(ProviderApplication application) {
        return ProviderApplicationResponse.builder()
                .id(application.getId())
                .userId(application.getUser().getId())
                .userDisplayName(application.getUser().getDisplayName())
                .userEmail(application.getUser().getEmail())
                .businessName(application.getBusinessName())
                .bio(application.getBio())
                .address(application.getAddress())
                .phoneNumber(application.getPhoneNumber())
                .website(application.getWebsite())
                .businessLicenseFileUrl(application.getBusinessLicenseFileUrl())
                .status(application.getStatus())
                .statusDisplayName(application.getStatus().getDisplayName())
                .rejectionReason(application.getRejectionReason())
                .submittedAt(application.getSubmittedAt())
                .reviewedAt(application.getReviewedAt())
                .reviewedByDisplayName(
                        application.getReviewedBy() != null ? application.getReviewedBy().getDisplayName() : null)
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
