package com.vietlong.spring_app.service;

import com.vietlong.spring_app.dto.request.CreateAppointmentRequest;
import com.vietlong.spring_app.dto.response.AppointmentResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.*;
import com.vietlong.spring_app.repository.AppointmentRepository;
import com.vietlong.spring_app.repository.ProviderScheduleRepository;
import com.vietlong.spring_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ProviderScheduleRepository providerScheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public AppointmentResponse createAppointment(String userId, CreateAppointmentRequest request) throws AppException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ProviderSchedule schedule = providerScheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.canBeBooked()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Schedule này không thể đặt lịch");
        }

        if (appointmentRepository.findActiveAppointmentByUserIdAndScheduleId(userId, request.getScheduleId())
                .isPresent()) {
            throw new AppException(ErrorCode.VALIDATION_ERROR, "Bạn đã đặt lịch này rồi");
        }

        Appointment appointment = Appointment.createFromSchedule(user, schedule, request.getNotesFromUser());
        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("User {} đã đặt lịch schedule {}", userId, request.getScheduleId());
        return convertToResponse(savedAppointment);
    }

    public List<AppointmentResponse> getUserAppointments(String userId) {
        List<Appointment> appointments = appointmentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<AppointmentResponse> getUserAppointmentsPaginated(String userId, Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return appointments.map(this::convertToResponse);
    }

    public List<AppointmentResponse> getUserAppointmentsByStatus(String userId, AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId,
                status);
        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(String appointmentId) throws AppException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        return convertToResponse(appointment);
    }

    @Transactional
    public AppointmentResponse confirmAppointment(String providerId, String appointmentId) throws AppException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        if (!appointment.getProviderSchedule().getProvider().getId().equals(providerId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Không có quyền xác nhận appointment này");
        }

        appointment.confirm();
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        log.info("Provider {} đã xác nhận appointment {}", providerId, appointmentId);
        return convertToResponse(updatedAppointment);
    }

    @Transactional
    public AppointmentResponse completeAppointment(String providerId, String appointmentId) throws AppException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        if (!appointment.getProviderSchedule().getProvider().getId().equals(providerId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Không có quyền hoàn thành appointment này");
        }

        appointment.complete();
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        ProviderSchedule schedule = appointment.getProviderSchedule();
        if (schedule.getRemainingSlots() == 0) {
            schedule.complete();
            providerScheduleRepository.save(schedule);
        }

        log.info("Provider {} đã hoàn thành appointment {}", providerId, appointmentId);
        return convertToResponse(updatedAppointment);
    }

    @Transactional
    public AppointmentResponse cancelAppointment(String userId, String appointmentId, String reason)
            throws AppException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        if (!appointment.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Không có quyền hủy appointment này");
        }

        appointment.cancel(reason);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        ProviderSchedule schedule = appointment.getProviderSchedule();
        schedule.cancel();
        providerScheduleRepository.save(schedule);

        log.info("User {} đã hủy appointment {}", userId, appointmentId);
        return convertToResponse(updatedAppointment);
    }

    public List<AppointmentResponse> getProviderAppointments(String providerId) {
        List<Appointment> appointments = appointmentRepository.findByProviderIdOrderByCreatedAtDesc(providerId);
        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Page<AppointmentResponse> getProviderAppointmentsPaginated(String providerId, Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findByProviderIdOrderByCreatedAtDesc(providerId,
                pageable);
        return appointments.map(this::convertToResponse);
    }

    public List<AppointmentResponse> getProviderAppointmentsByStatus(String providerId, AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByProviderIdAndStatusOrderByCreatedAtDesc(providerId,
                status);
        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private AppointmentResponse convertToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .userId(appointment.getUser().getId())
                .userName(appointment.getUser().getDisplayName())
                .scheduleId(appointment.getProviderSchedule().getId())
                .providerName(appointment.getProviderSchedule().getProvider().getDisplayName())
                .serviceId(appointment.getProviderSchedule().getService().getId())
                .serviceName(appointment.getProviderSchedule().getService().getServiceName())
                .startTime(appointment.getProviderSchedule().getStartTime())
                .endTime(appointment.getProviderSchedule().getEndTime())
                .status(appointment.getStatus())
                .notesFromUser(appointment.getNotesFromUser())
                .cancellationReason(appointment.getCancellationReason())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}
