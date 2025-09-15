package com.vietlong.spring_app.repository;

import com.vietlong.spring_app.model.ProviderSchedule;
import com.vietlong.spring_app.model.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProviderScheduleRepository extends JpaRepository<ProviderSchedule, String> {

        List<ProviderSchedule> findByProviderIdOrderByStartTimeAsc(String providerId);

        List<ProviderSchedule> findByServiceIdOrderByStartTimeAsc(String serviceId);

        @Query("SELECT s FROM ProviderSchedule s WHERE s.startTime >= :startTime AND s.endTime <= :endTime " +
                        "AND s.status = :status AND s.remainingSlots > 0 ORDER BY s.startTime ASC")
        List<ProviderSchedule> findAvailableSchedulesInTimeRange(
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime,
                        @Param("status") ScheduleStatus status);

        @Query("SELECT s FROM ProviderSchedule s WHERE s.provider.id = :providerId " +
                        "AND s.startTime >= :startTime AND s.endTime <= :endTime ORDER BY s.startTime ASC")
        List<ProviderSchedule> findByProviderAndTimeRange(
                        @Param("providerId") String providerId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        @Query("SELECT s FROM ProviderSchedule s WHERE s.service.id = :serviceId " +
                        "AND s.startTime >= :startTime AND s.endTime <= :endTime ORDER BY s.startTime ASC")
        List<ProviderSchedule> findByServiceAndTimeRange(
                        @Param("serviceId") String serviceId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        @Query("SELECT s FROM ProviderSchedule s WHERE s.status = 'AVAILABLE' " +
                        "AND s.remainingSlots > 0 AND s.startTime > :now ORDER BY s.startTime ASC")
        List<ProviderSchedule> findBookableSchedules(@Param("now") LocalDateTime now);

        Page<ProviderSchedule> findByProviderIdOrderByStartTimeAsc(String providerId, Pageable pageable);

        List<ProviderSchedule> findByStatusOrderByStartTimeAsc(ScheduleStatus status);

        @Query("SELECT s FROM ProviderSchedule s WHERE s.provider.id = :providerId " +
                        "AND s.id != :excludeId AND " +
                        "(s.startTime < :endTime AND s.endTime > :startTime)")
        List<ProviderSchedule> findConflictingSchedules(
                        @Param("providerId") String providerId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime,
                        @Param("excludeId") String excludeId);
}
