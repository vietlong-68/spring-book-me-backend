package com.vietlong.spring_app.service;

import com.vietlong.spring_app.dto.request.CreateScheduleRequest;
import com.vietlong.spring_app.dto.request.UpdateScheduleRequest;
import com.vietlong.spring_app.dto.response.ProviderScheduleResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.*;
import com.vietlong.spring_app.repository.ProviderScheduleRepository;
import com.vietlong.spring_app.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderScheduleService {

    private final ProviderScheduleRepository providerScheduleRepository;
    private final ServiceRepository serviceRepository;

    @Transactional
    public ProviderScheduleResponse createSchedule(String providerId, CreateScheduleRequest request)
            throws AppException {

        com.vietlong.spring_app.model.Service service = serviceRepository
                .findByIdWithProviderAndUser(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        if (!service.getProvider().getUser().getId().equals(providerId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Service không thuộc về provider này");
        }

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Thời gian bắt đầu phải trước thời gian kết thúc");
        }

        List<ProviderSchedule> conflicts = providerScheduleRepository.findConflictingSchedules(
                providerId, request.getStartTime(), request.getEndTime(), "");

        if (!conflicts.isEmpty()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Thời gian này đã có lịch khác");
        }

        ProviderSchedule schedule = ProviderSchedule.builder()
                .provider(service.getProvider().getUser())
                .service(service)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxCapacity(request.getMaxCapacity())
                .remainingSlots(request.getMaxCapacity())
                .notes(request.getNotes())
                .build();

        ProviderSchedule savedSchedule = providerScheduleRepository.save(schedule);
        return convertToResponse(savedSchedule);
    }

    public List<ProviderScheduleResponse> getAvailableSchedules(String providerId, String serviceId,
            LocalDateTime startTime, LocalDateTime endTime) {
        List<ProviderSchedule> schedules;

        if (providerId != null && serviceId != null) {
            schedules = providerScheduleRepository.findByProviderAndTimeRange(providerId, startTime, endTime);
        } else if (providerId != null) {
            schedules = providerScheduleRepository.findByProviderAndTimeRange(providerId, startTime, endTime);
        } else if (serviceId != null) {
            schedules = providerScheduleRepository.findByServiceAndTimeRange(serviceId, startTime, endTime);
        } else {
            schedules = providerScheduleRepository.findAvailableSchedulesInTimeRange(
                    startTime, endTime, ScheduleStatus.AVAILABLE);
        }

        return schedules.stream()
                .filter(schedule -> schedule.getRemainingSlots() > 0)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProviderScheduleResponse getScheduleById(String scheduleId) throws AppException {
        ProviderSchedule schedule = providerScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));
        return convertToResponse(schedule);
    }

    @Transactional
    public ProviderScheduleResponse updateSchedule(String providerId, String scheduleId,
            UpdateScheduleRequest request) throws AppException {
        ProviderSchedule schedule = providerScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getProvider().getId().equals(providerId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Không có quyền cập nhật schedule này");
        }

        if (request.getStartTime() != null) {
            schedule.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            schedule.setEndTime(request.getEndTime());
        }
        if (request.getMaxCapacity() != null) {

            int currentBookings = schedule.getCurrentBookings();
            schedule.setMaxCapacity(request.getMaxCapacity());
            schedule.setRemainingSlots(Math.max(0, request.getMaxCapacity() - currentBookings));
        }
        if (request.getNotes() != null) {
            schedule.setNotes(request.getNotes());
        }

        ProviderSchedule updatedSchedule = providerScheduleRepository.save(schedule);
        return convertToResponse(updatedSchedule);
    }

    @Transactional
    public void deleteSchedule(String providerId, String scheduleId) throws AppException {
        ProviderSchedule schedule = providerScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getProvider().getId().equals(providerId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Không có quyền xóa schedule này");
        }

        if (schedule.getCurrentBookings() > 0) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Không thể xóa schedule đã có appointment");
        }

        providerScheduleRepository.delete(schedule);
    }

    public List<ProviderScheduleResponse> getProviderSchedules(String providerId) {
        List<ProviderSchedule> schedules = providerScheduleRepository.findByProviderIdOrderByStartTimeAsc(providerId);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<ProviderScheduleResponse> getProviderSchedulesPaginated(String providerId, Pageable pageable) {
        Page<ProviderSchedule> schedules = providerScheduleRepository.findByProviderIdOrderByStartTimeAsc(providerId,
                pageable);
        return schedules.map(this::convertToResponse);
    }

    private ProviderScheduleResponse convertToResponse(ProviderSchedule schedule) {
        return ProviderScheduleResponse.builder()
                .id(schedule.getId())
                .providerId(schedule.getProvider().getId())
                .providerName(schedule.getProvider().getDisplayName())
                .serviceId(schedule.getService().getId())
                .serviceName(schedule.getService().getServiceName())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .maxCapacity(schedule.getMaxCapacity())
                .remainingSlots(schedule.getRemainingSlots())
                .status(schedule.getStatus())
                .notes(schedule.getNotes())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
