package com.vietlong.spring_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "id", "startTime", "endTime", "status", "maxCapacity", "remainingSlots" })
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(name = "provider_schedules", indexes = {
        @Index(name = "idx_schedule_provider_id", columnList = "provider_id"),
        @Index(name = "idx_schedule_service_id", columnList = "service_id"),
        @Index(name = "idx_schedule_status", columnList = "status"),
        @Index(name = "idx_schedule_start_time", columnList = "start_time"),
        @Index(name = "idx_schedule_provider_start", columnList = "provider_id, start_time")
})
public class ProviderSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "remaining_slots", nullable = false)
    private Integer remainingSlots;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ScheduleStatus status = ScheduleStatus.AVAILABLE;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.maxCapacity != null && this.remainingSlots == null) {
            this.remainingSlots = this.maxCapacity;
        }
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return this.status.isAvailable() && this.remainingSlots > 0;
    }

    public boolean isFullyBooked() {
        return this.remainingSlots <= 0;
    }

    public boolean canBeBooked() {
        return this.status.canBeBooked() && !this.isFullyBooked();
    }

    public boolean canBeCancelled() {
        return this.status.canBeCancelled();
    }

    public void book() {
        if (this.canBeBooked()) {
            this.remainingSlots--;
            if (this.isFullyBooked()) {
                this.status = ScheduleStatus.BOOKED;
            }
        } else {
            throw new IllegalStateException("Cannot book schedule: " + this.status + " or fully booked");
        }
    }

    public void cancel() {
        if (this.canBeCancelled()) {
            if (this.remainingSlots < this.maxCapacity) {
                this.remainingSlots++;
            }
            if (this.remainingSlots > 0) {
                this.status = ScheduleStatus.AVAILABLE;
            } else {
                this.status = ScheduleStatus.CANCELLED;
            }
        } else {
            throw new IllegalStateException("Cannot cancel schedule with status: " + this.status);
        }
    }

    public void complete() {
        if (this.status == ScheduleStatus.BOOKED) {
            this.status = ScheduleStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Cannot complete schedule with status: " + this.status);
        }
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
        if (this.provider == null) {
            return false;
        }
        if (this.service == null) {
            return false;
        }
        if (!this.isValidRemainingSlots()) {
            return false;
        }
        return this.canBeBooked();
    }

    public boolean isOverlapping(LocalDateTime start, LocalDateTime end) {
        return this.startTime.isBefore(end) && this.endTime.isAfter(start);
    }

    public boolean conflictsWith(ProviderSchedule other) {
        if (other == null || other.getId().equals(this.getId())) {
            return false;
        }
        return this.isOverlapping(other.startTime, other.endTime);
    }

    public Integer getCurrentBookings() {
        if (this.maxCapacity == null || this.remainingSlots == null) {
            return 0;
        }
        return Math.max(0, this.maxCapacity - this.remainingSlots);
    }

    public boolean isValidRemainingSlots() {
        if (this.maxCapacity == null || this.remainingSlots == null) {
            return false;
        }
        return this.remainingSlots >= 0 && this.remainingSlots <= this.maxCapacity;
    }

    public void resetSlots() {
        if (this.maxCapacity != null) {
            this.remainingSlots = this.maxCapacity;
            this.status = ScheduleStatus.AVAILABLE;
        }
    }
}
