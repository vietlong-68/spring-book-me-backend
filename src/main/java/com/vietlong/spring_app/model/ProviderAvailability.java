package com.vietlong.spring_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "id", "dayOfWeek", "startTime", "endTime" })
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(name = "provider_availability", indexes = {
        @Index(name = "idx_availability_provider_id", columnList = "provider_id"),
        @Index(name = "idx_availability_day_of_week", columnList = "day_of_week"),
        @Index(name = "idx_availability_provider_day", columnList = "provider_id, day_of_week", unique = true)
})
public class ProviderAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

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

    public boolean isValidTimeRange() {
        return this.startTime != null && this.endTime != null &&
                this.startTime.isBefore(this.endTime);
    }

    public boolean isWorkingDay() {
        return this.dayOfWeek != null;
    }

    public String getDayOfWeekName() {
        return this.dayOfWeek != null ? this.dayOfWeek.getDisplayName() : null;
    }

    public boolean isWeekday() {
        return this.dayOfWeek != null && this.dayOfWeek.isWeekday();
    }

    public boolean isWeekend() {
        return this.dayOfWeek != null && this.dayOfWeek.isWeekend();
    }
}
