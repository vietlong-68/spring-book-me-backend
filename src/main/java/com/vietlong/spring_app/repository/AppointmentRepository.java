package com.vietlong.spring_app.repository;

import com.vietlong.spring_app.model.Appointment;
import com.vietlong.spring_app.model.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

        List<Appointment> findByUserIdOrderByCreatedAtDesc(String userId);

        List<Appointment> findByProviderScheduleIdOrderByCreatedAtDesc(String scheduleId);

        @Query("SELECT a FROM Appointment a WHERE a.providerSchedule.provider.id = :providerId ORDER BY a.createdAt DESC")
        List<Appointment> findByProviderIdOrderByCreatedAtDesc(@Param("providerId") String providerId);

        List<Appointment> findByStatusOrderByCreatedAtDesc(AppointmentStatus status);

        List<Appointment> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, AppointmentStatus status);

        @Query("SELECT a FROM Appointment a WHERE a.providerSchedule.provider.id = :providerId " +
                        "AND a.status = :status ORDER BY a.createdAt DESC")
        List<Appointment> findByProviderIdAndStatusOrderByCreatedAtDesc(
                        @Param("providerId") String providerId,
                        @Param("status") AppointmentStatus status);

        Page<Appointment> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

        @Query("SELECT a FROM Appointment a WHERE a.providerSchedule.provider.id = :providerId ORDER BY a.createdAt DESC")
        Page<Appointment> findByProviderIdOrderByCreatedAtDesc(@Param("providerId") String providerId,
                        Pageable pageable);

        @Query("SELECT a FROM Appointment a WHERE a.providerSchedule.startTime >= :startTime " +
                        "AND a.providerSchedule.endTime <= :endTime ORDER BY a.providerSchedule.startTime ASC")
        List<Appointment> findAppointmentsInTimeRange(
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        long countByUserId(String userId);

        @Query("SELECT COUNT(a) FROM Appointment a WHERE a.providerSchedule.provider.id = :providerId")
        long countByProviderId(@Param("providerId") String providerId);

        Optional<Appointment> findByUserIdAndProviderScheduleId(String userId, String scheduleId);
}
