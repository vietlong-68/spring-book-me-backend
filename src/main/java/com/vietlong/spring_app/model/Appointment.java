package com.vietlong.spring_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "id", "status" })
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appointment_user_id", columnList = "user_id"),
        @Index(name = "idx_appointment_schedule_id", columnList = "provider_schedule_id"),
        @Index(name = "idx_appointment_status", columnList = "status")
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
    @JoinColumn(name = "provider_schedule_id", nullable = false)
    private ProviderSchedule providerSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

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

    public void complete() {
        if (this.canBeCompleted()) {
            this.status = AppointmentStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Cannot complete appointment with status: " + this.status);
        }
    }

    public boolean isInPast() {
        return this.providerSchedule.getEndTime().isBefore(LocalDateTime.now());
    }

    public boolean isInFuture() {
        return this.providerSchedule.getStartTime().isAfter(LocalDateTime.now());
    }

    public boolean isToday() {
        LocalDateTime now = LocalDateTime.now();
        return this.providerSchedule.getStartTime().toLocalDate().equals(now.toLocalDate());
    }

    public boolean isValidForBooking() {
        if (this.providerSchedule == null) {
            return false;
        }
        if (!this.providerSchedule.isValidForBooking()) {
            return false;
        }
        return true;
    }

    /**
     * Tạo appointment từ ProviderSchedule
     */
    public static Appointment createFromSchedule(User user, ProviderSchedule schedule, String notes) {
        if (!schedule.canBeBooked()) {
            throw new IllegalStateException("Cannot create appointment from unavailable schedule");
        }

        Appointment appointment = Appointment.builder()
                .user(user)
                .providerSchedule(schedule)
                .status(AppointmentStatus.SCHEDULED)
                .notesFromUser(notes)
                .build();

        // Book slot trong schedule
        schedule.book();

        return appointment;
    }
}
