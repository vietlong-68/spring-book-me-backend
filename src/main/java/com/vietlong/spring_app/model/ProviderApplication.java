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
@Table(name = "provider_applications", indexes = {
        @Index(name = "idx_provider_app_user_id", columnList = "user_id"),
        @Index(name = "idx_provider_app_status", columnList = "status"),
        @Index(name = "idx_provider_app_submitted_at", columnList = "submitted_at")
})
public class ProviderApplication {

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

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "business_license_file_url", length = 500)
    private String businessLicenseFileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProviderApplicationStatus status;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = ProviderApplicationStatus.PENDING;
        }
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return this.status == ProviderApplicationStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == ProviderApplicationStatus.APPROVED;
    }

    public boolean isRejected() {
        return this.status == ProviderApplicationStatus.REJECTED;
    }

    public boolean isCancelled() {
        return this.status == ProviderApplicationStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return this.status == ProviderApplicationStatus.COMPLETED;
    }

    public boolean canBeEdited() {
        return this.status == ProviderApplicationStatus.PENDING;
    }

    public boolean canBeCancelled() {
        return this.status == ProviderApplicationStatus.PENDING;
    }

    public boolean canBeReviewed() {
        return this.status == ProviderApplicationStatus.PENDING;
    }

    public void approve(User admin) {
        if (!this.canBeReviewed()) {
            throw new IllegalStateException("Cannot approve application with status: " + this.status);
        }
        this.status = ProviderApplicationStatus.APPROVED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = admin;
        this.rejectionReason = null;
    }

    public void reject(User admin, String reason) {
        if (!this.canBeReviewed()) {
            throw new IllegalStateException("Cannot reject application with status: " + this.status);
        }
        this.status = ProviderApplicationStatus.REJECTED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = admin;
        this.rejectionReason = reason;
    }

    public void cancel() {
        if (!this.canBeCancelled()) {
            throw new IllegalStateException("Cannot cancel application with status: " + this.status);
        }
        this.status = ProviderApplicationStatus.CANCELLED;
    }

    public void complete() {
        if (!this.isApproved()) {
            throw new IllegalStateException("Cannot complete application with status: " + this.status);
        }
        this.status = ProviderApplicationStatus.COMPLETED;
    }
}
