package com.vietlong.spring_app.dto.response;

import com.vietlong.spring_app.model.Provider;
import com.vietlong.spring_app.model.ProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponse {

    private String id;
    private String userId;
    private String userDisplayName;
    private String userEmail;
    private String businessName;
    private String bio;
    private String address;
    private String phoneNumber;
    private String website;
    private String logoUrl;
    private String bannerUrl;
    private ProviderStatus status;
    private String statusDisplayName;
    private Boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProviderResponse fromEntity(Provider provider) {
        return ProviderResponse.builder()
                .id(provider.getId())
                .userId(provider.getUser().getId())
                .userDisplayName(provider.getUser().getDisplayName())
                .userEmail(provider.getUser().getEmail())
                .businessName(provider.getBusinessName())
                .bio(provider.getBio())
                .address(provider.getAddress())
                .phoneNumber(provider.getPhoneNumber())
                .website(provider.getWebsiteUrl())
                .logoUrl(provider.getLogoUrl())
                .bannerUrl(provider.getBannerUrl())
                .status(provider.getStatus())
                .statusDisplayName(provider.getStatus().getDisplayName())
                .isVerified(provider.getIsVerified())
                .createdAt(provider.getCreatedAt())
                .updatedAt(provider.getUpdatedAt())
                .build();
    }
}
