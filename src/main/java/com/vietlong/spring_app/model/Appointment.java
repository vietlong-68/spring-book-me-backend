package com.vietlong.spring_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "id", "startTime", "endTime", "status", "finalPrice" })
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appointment_user_id", columnList = "user_id"),
        @Index(name = "idx_appointment_provider_id", columnList = "provider_id"),
        @Index(name = "idx_appointment_service_id", columnList = "service_id"),
        @Index(name = "idx_appointment_status", columnList = "status"),
        @Index(name = "idx_appointment_start_time", columnList = "start_time"),
        @Index(name = "idx_appointment_provider_start", columnList = "provider_id, start_time")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_appointment_provider_time", columnNames = { "provider_id", "start_time" })
})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(name = "final_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "notes_from_user", columnDefinition = "TEXT")
    private String notesFromUser;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
        return this.status.isActive();
    }

    public boolean isCompleted() {
        return this.status.isCompleted();
    }

    public boolean isCancelled() {
        return this.status.isCancelled();
    }

    public boolean canBeCancelled() {
        return this.status.canBeCancelled();
    }

    public boolean canBeConfirmed() {
        return this.status.canBeConfirmed();
    }

    public boolean canBeCompleted() {
        return this.status.canBeCompleted();
    }

    public void confirm() {
        if (this.canBeConfirmed()) {
            if (!this.provider.isActive()) {
                throw new IllegalStateException("Cannot confirm appointment: Provider is not active");
            }
            if (!this.service.isActive()) {
                throw new IllegalStateException("Cannot confirm appointment: Service is not active");
            }
            this.status = AppointmentStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("Cannot confirm appointment with status: " + this.status);
        }
    }

    public void cancel(String reason) {
        if (this.canBeCancelled()) {
            this.status = AppointmentStatus.CANCELLED;
            this.cancellationReason = reason;
        } else {
            throw new IllegalStateException("Cannot cancel appointment with status: " + this.status);
        }
    }

    public void markAsNoShow() {
        if (this.status == AppointmentStatus.CONFIRMED || this.status == AppointmentStatus.IN_PROGRESS) {
            this.status = AppointmentStatus.NO_SHOW;
        } else {
            throw new IllegalStateException("Cannot mark as no-show appointment with status: " + this.status);
        }
    }

    public void start() {
        if (this.status == AppointmentStatus.CONFIRMED) {
            this.status = AppointmentStatus.IN_PROGRESS;
        } else {
            throw new IllegalStateException("Cannot start appointment with status: " + this.status);
        }
    }

    public void complete() {
        if (this.canBeCompleted()) {
            this.status = AppointmentStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Cannot complete appointment with status: " + this.status);
        }
    }

    public boolean isOverlapping(LocalDateTime start, LocalDateTime end) {
        return this.startTime.isBefore(end) && this.endTime.isAfter(start);
    }

    public boolean isInPast() {
        return this.endTime.isBefore(LocalDateTime.now());
    }

    public boolean isInFuture() {
        return this.startTime.isAfter(LocalDateTime.now());
    }

    public boolean isToday() {
        LocalDateTime now = LocalDateTime.now();
        return this.startTime.toLocalDate().equals(now.toLocalDate());
    }

    public boolean isValidForBooking() {
        if (this.startTime == null || this.endTime == null) {
            return false;
        }
        if (!this.startTime.isBefore(this.endTime)) {
            return false;
        }
        if (this.startTime.isBefore(LocalDateTime.now())) {
            return false;
        }
        if (this.provider == null || !this.provider.isActive()) {
            return false;
        }
        if (this.service == null || !this.service.isActive()) {
            return false;
        }
        return true;
    }

    public boolean conflictsWith(Appointment other) {
        if (other == null || other.getId().equals(this.getId())) {
            return false;
        }
        return this.isOverlapping(other.startTime, other.endTime);
    }
}
