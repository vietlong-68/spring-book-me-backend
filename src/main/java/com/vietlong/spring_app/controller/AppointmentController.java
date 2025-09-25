package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.CreateAppointmentRequest;
import com.vietlong.spring_app.dto.response.AppointmentResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.model.AppointmentStatus;
import com.vietlong.spring_app.service.AppointmentService;
import com.vietlong.spring_app.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller xử lý các API liên quan đến quản lý cuộc hẹn (appointment)
 * Bao gồm: đặt lịch, xác nhận, hoàn thành, hủy lịch, xem lịch sử
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AuthService authService;

    /**
     * API đặt lịch hẹn mới
     * Yêu cầu: Chỉ USER mới có quyền truy cập
     * 
     * @param createRequest  Thông tin đặt lịch (scheduleId, notesFromUser)
     * @param authentication Thông tin xác thực của user
     * @param request        HttpServletRequest
     * @return Thông tin cuộc hẹn đã tạo
     * @throws AppException nếu schedule không khả dụng hoặc user đã đặt lịch này
     */
    @PostMapping("/appointments")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest createRequest,
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String userId = authService.getUserIdFromAuthentication(authentication);
        AppointmentResponse appointment = appointmentService.createAppointment(userId, createRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(appointment, "Đặt lịch thành công", request));
    }

    /**
     * API lấy danh sách tất cả lịch hẹn của user
     * Cho phép tất cả các role truy cập
     * 
     * @param authentication Thông tin xác thực của user
     * @param request        HttpServletRequest
     * @return Danh sách tất cả lịch hẹn của user
     */
    @GetMapping("/user/appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getUserAppointments(
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String userId = authService.getUserIdFromAuthentication(authentication);
        List<AppointmentResponse> appointments = appointmentService.getUserAppointments(userId);

        return ResponseEntity.ok(ApiResponse.success(appointments, "Lấy danh sách lịch hẹn thành công", request));
    }

    /**
     * API lấy danh sách lịch hẹn của user có phân trang và sắp xếp
     * Cho phép tất cả các role truy cập
     * 
     * @param authentication Thông tin xác thực của user
     * @param pageable       Thông tin phân trang (page, size, sort)
     * @param request        HttpServletRequest
     * @return Page chứa danh sách lịch hẹn đã phân trang
     */
    @GetMapping("/user/appointments/paginated")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getUserAppointmentsPaginated(
            Authentication authentication,
            Pageable pageable,
            HttpServletRequest request) throws AppException {

        String userId = authService.getUserIdFromAuthentication(authentication);
        Page<AppointmentResponse> appointments = appointmentService.getUserAppointmentsPaginated(userId, pageable);

        return ResponseEntity
                .ok(ApiResponse.success(appointments, "Lấy danh sách lịch hẹn có phân trang thành công", request));
    }

    /**
     * API lấy danh sách lịch hẹn của user theo trạng thái
     * Cho phép tất cả các role truy cập
     * 
     * @param status         Trạng thái lịch hẹn (SCHEDULED, CONFIRMED, COMPLETED,
     *                       CANCELLED)
     * @param authentication Thông tin xác thực của user
     * @param request        HttpServletRequest
     * @return Danh sách lịch hẹn theo trạng thái
     */
    @GetMapping("/user/appointments/status/{status}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getUserAppointmentsByStatus(
            @PathVariable("status") AppointmentStatus status,
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String userId = authService.getUserIdFromAuthentication(authentication);
        List<AppointmentResponse> appointments = appointmentService.getUserAppointmentsByStatus(userId, status);

        return ResponseEntity
                .ok(ApiResponse.success(appointments, "Lấy danh sách lịch hẹn theo trạng thái thành công", request));
    }

    /**
     * API lấy chi tiết thông tin cuộc hẹn
     * 
     * @param appointmentId ID của cuộc hẹn
     * @param request       HttpServletRequest
     * @return Thông tin chi tiết cuộc hẹn
     * @throws AppException nếu không tìm thấy cuộc hẹn
     */
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(
            @PathVariable("appointmentId") String appointmentId,
            HttpServletRequest request) throws AppException {

        AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(ApiResponse.success(appointment, "Lấy chi tiết lịch hẹn thành công", request));
    }

    /**
     * API xác nhận lịch hẹn
     * Yêu cầu: Chỉ PROVIDER sở hữu lịch hẹn mới có quyền truy cập
     * 
     * @param appointmentId  ID của cuộc hẹn cần xác nhận
     * @param authentication Thông tin xác thực của provider
     * @param request        HttpServletRequest
     * @return Thông tin cuộc hẹn đã xác nhận
     * @throws AppException nếu không có quyền xác nhận hoặc cuộc hẹn không tồn tại
     */
    @PutMapping("/appointments/{appointmentId}/confirm")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(
            @PathVariable("appointmentId") String appointmentId,
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String providerId = authService.getUserIdFromAuthentication(authentication);
        AppointmentResponse appointment = appointmentService.confirmAppointment(providerId, appointmentId);

        return ResponseEntity.ok(ApiResponse.success(appointment, "Xác nhận lịch hẹn thành công", request));
    }

    /**
     * API hoàn thành dịch vụ
     * Yêu cầu: Chỉ PROVIDER sở hữu lịch hẹn mới có quyền truy cập
     * 
     * @param appointmentId  ID của cuộc hẹn cần hoàn thành
     * @param authentication Thông tin xác thực của provider
     * @param request        HttpServletRequest
     * @return Thông tin cuộc hẹn đã hoàn thành
     * @throws AppException nếu không có quyền hoàn thành hoặc cuộc hẹn không tồn
     *                      tại
     */
    @PutMapping("/appointments/{appointmentId}/complete")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> completeAppointment(
            @PathVariable("appointmentId") String appointmentId,
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String providerId = authService.getUserIdFromAuthentication(authentication);
        AppointmentResponse appointment = appointmentService.completeAppointment(providerId, appointmentId);

        return ResponseEntity.ok(ApiResponse.success(appointment, "Hoàn thành dịch vụ thành công", request));
    }

    /**
     * API hủy lịch hẹn
     * 
     * @param appointmentId  ID của cuộc hẹn cần hủy
     * @param cancelRequest  Lý do hủy lịch hẹn (tùy chọn)
     * @param authentication Thông tin xác thực của user
     * @param request        HttpServletRequest
     * @return Thông tin cuộc hẹn đã hủy
     * @throws AppException nếu không có quyền hủy hoặc cuộc hẹn không tồn tại
     */
    @PutMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(
            @PathVariable("appointmentId") String appointmentId,
            @RequestBody(required = false) Map<String, String> cancelRequest,
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String userId = authService.getUserIdFromAuthentication(authentication);
        String reason = cancelRequest != null ? cancelRequest.get("reason") : "Không có lý do";

        AppointmentResponse appointment = appointmentService.cancelAppointment(userId, appointmentId, reason);

        return ResponseEntity.ok(ApiResponse.success(appointment, "Hủy lịch hẹn thành công", request));
    }

    /**
     * API lấy danh sách tất cả lịch hẹn của provider
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập
     * 
     * @param authentication Thông tin xác thực của provider
     * @param request        HttpServletRequest
     * @return Danh sách tất cả lịch hẹn của provider
     */
    @GetMapping("/provider/appointments")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getProviderAppointments(
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String providerId = authService.getUserIdFromAuthentication(authentication);
        List<AppointmentResponse> appointments = appointmentService.getProviderAppointments(providerId);

        return ResponseEntity.ok(ApiResponse.success(appointments, "Lấy danh sách lịch hẹn thành công", request));
    }

    /**
     * API lấy danh sách lịch hẹn của provider có phân trang và sắp xếp
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập
     * 
     * @param authentication Thông tin xác thực của provider
     * @param pageable       Thông tin phân trang (page, size, sort)
     * @param request        HttpServletRequest
     * @return Page chứa danh sách lịch hẹn đã phân trang
     */
    @GetMapping("/provider/appointments/paginated")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getProviderAppointmentsPaginated(
            Authentication authentication,
            Pageable pageable,
            HttpServletRequest request) throws AppException {

        String providerId = authService.getUserIdFromAuthentication(authentication);
        Page<AppointmentResponse> appointments = appointmentService.getProviderAppointmentsPaginated(providerId,
                pageable);

        return ResponseEntity
                .ok(ApiResponse.success(appointments, "Lấy danh sách lịch hẹn có phân trang thành công", request));
    }

    /**
     * API lấy danh sách lịch hẹn của provider theo trạng thái
     * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập
     * 
     * @param status         Trạng thái lịch hẹn (SCHEDULED, CONFIRMED, COMPLETED,
     *                       CANCELLED)
     * @param authentication Thông tin xác thực của provider
     * @param request        HttpServletRequest
     * @return Danh sách lịch hẹn theo trạng thái
     */
    @GetMapping("/provider/appointments/status/{status}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getProviderAppointmentsByStatus(
            @PathVariable("status") AppointmentStatus status,
            Authentication authentication,
            HttpServletRequest request) throws AppException {

        String providerId = authService.getUserIdFromAuthentication(authentication);
        List<AppointmentResponse> appointments = appointmentService.getProviderAppointmentsByStatus(providerId, status);

        return ResponseEntity
                .ok(ApiResponse.success(appointments, "Lấy danh sách lịch hẹn theo trạng thái thành công", request));
    }
}
