package com.vietlong.spring_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "id", "businessName", "status" })
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(name = "providers", indexes = {
        @Index(name = "idx_provider_user_id", columnList = "user_id"),
        @Index(name = "idx_provider_status", columnList = "status"),
        @Index(name = "idx_provider_verified", columnList = "is_verified")
})
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "business_name", nullable = false, length = 255)
    private String businessName;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "website", length = 500)
    private String websiteUrl;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProviderStatus status = ProviderStatus.PENDING_APPROVAL;

    @Builder.Default
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == ProviderStatus.ACTIVE;
    }

    public boolean isPending() {
        return this.status == ProviderStatus.PENDING_APPROVAL;
    }

    public boolean isSuspended() {
        return this.status == ProviderStatus.SUSPENDED;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
    }
}
