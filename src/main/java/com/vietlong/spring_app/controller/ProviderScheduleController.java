package com.vietlong.spring_app.controller;

import com.vietlong.spring_app.common.ApiResponse;
import com.vietlong.spring_app.dto.request.CreateScheduleRequest;
import com.vietlong.spring_app.dto.request.CreateRecurringScheduleRequest;
import com.vietlong.spring_app.dto.request.UpdateScheduleRequest;
import com.vietlong.spring_app.dto.response.ProviderScheduleResponse;
import com.vietlong.spring_app.dto.response.RecurringScheduleResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.service.AuthService;
import com.vietlong.spring_app.service.ProviderScheduleService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller xử lý các API liên quan đến quản lý lịch hẹn của Provider
 * Bao gồm: tạo lịch, cập nhật lịch, xóa lịch, tìm kiếm lịch trống
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProviderScheduleController {

        private final ProviderScheduleService providerScheduleService;
        private final AuthService authService;

        /**
         * API tìm kiếm lịch trống có thể đặt
         * 
         * @param providerId ID của provider (tùy chọn)
         * @param serviceId  ID của dịch vụ (tùy chọn)
         * @param startTime  Thời gian bắt đầu tìm kiếm (tùy chọn, mặc định là hiện tại)
         * @param endTime    Thời gian kết thúc tìm kiếm (tùy chọn, mặc định là 30 ngày
         *                   sau)
         * @param request    HttpServletRequest
         * @return Danh sách lịch trống có thể đặt
         * @throws AppException nếu có lỗi khi tìm kiếm
         */
        @GetMapping("/schedules/available")
        public ResponseEntity<ApiResponse<List<ProviderScheduleResponse>>> getAvailableSchedules(
                        @RequestParam(required = false) String providerId,
                        @RequestParam(required = false) String serviceId,
                        @RequestParam(required = false) LocalDateTime startTime,
                        @RequestParam(required = false) LocalDateTime endTime,
                        HttpServletRequest request) throws AppException {

                // Mặc định tìm trong 30 ngày tới nếu không có thời gian
                if (startTime == null)
                        startTime = LocalDateTime.now();
                if (endTime == null)
                        endTime = startTime.plusDays(30);

                List<ProviderScheduleResponse> schedules = providerScheduleService.getAvailableSchedules(
                                providerId, serviceId, startTime, endTime);

                return ResponseEntity
                                .ok(ApiResponse.success(schedules, "Lấy danh sách lịch trống thành công", request));
        }

        /**
         * API lấy chi tiết thông tin lịch hẹn
         * 
         * @param scheduleId ID của lịch hẹn
         * @param request    HttpServletRequest
         * @return Thông tin chi tiết lịch hẹn
         * @throws AppException nếu không tìm thấy lịch hẹn
         */
        @GetMapping("/schedules/{scheduleId}")
        public ResponseEntity<ApiResponse<ProviderScheduleResponse>> getScheduleById(
                        @PathVariable String scheduleId,
                        HttpServletRequest request) throws AppException {

                ProviderScheduleResponse schedule = providerScheduleService.getScheduleById(scheduleId);
                return ResponseEntity.ok(ApiResponse.success(schedule, "Lấy chi tiết lịch thành công", request));
        }

        /**
         * API tạo lịch hẹn mới cho provider
         * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập
         * 
         * @param createRequest  Thông tin tạo lịch hẹn (serviceId, startTime, endTime,
         *                       maxCapacity, notes)
         * @param authentication Thông tin xác thực của provider
         * @param request        HttpServletRequest
         * @return Thông tin lịch hẹn đã tạo
         * @throws AppException nếu service không thuộc về provider hoặc thời gian không
         *                      hợp lệ
         */
        @PostMapping("/provider/schedules")
        @PreAuthorize("hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<ProviderScheduleResponse>> createSchedule(
                        @Valid @RequestBody CreateScheduleRequest createRequest,
                        Authentication authentication,
                        HttpServletRequest request) throws AppException {

                String providerId = authService.getUserIdFromAuthentication(authentication);
                ProviderScheduleResponse schedule = providerScheduleService.createSchedule(providerId, createRequest);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(schedule, "Tạo lịch thành công", request));
        }

        /**
         * API cập nhật thông tin lịch hẹn
         * Yêu cầu: Chỉ PROVIDER sở hữu lịch hẹn mới có quyền truy cập
         * 
         * @param scheduleId     ID của lịch hẹn cần cập nhật
         * @param updateRequest  Thông tin cập nhật (startTime, endTime, maxCapacity,
         *                       notes)
         * @param authentication Thông tin xác thực của provider
         * @param request        HttpServletRequest
         * @return Thông tin lịch hẹn đã cập nhật
         * @throws AppException nếu không có quyền cập nhật hoặc lịch hẹn không tồn tại
         */
        @PutMapping("/provider/schedules/{scheduleId}")
        @PreAuthorize("hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<ProviderScheduleResponse>> updateSchedule(
                        @PathVariable String scheduleId,
                        @Valid @RequestBody UpdateScheduleRequest updateRequest,
                        Authentication authentication,
                        HttpServletRequest request) throws AppException {

                String providerId = authService.getUserIdFromAuthentication(authentication);
                ProviderScheduleResponse schedule = providerScheduleService.updateSchedule(providerId, scheduleId,
                                updateRequest);

                return ResponseEntity.ok(ApiResponse.success(schedule, "Cập nhật lịch thành công", request));
        }

        /**
         * API xóa lịch hẹn
         * Yêu cầu: Chỉ PROVIDER sở hữu lịch hẹn mới có quyền truy cập
         * 
         * @param scheduleId     ID của lịch hẹn cần xóa
         * @param authentication Thông tin xác thực của provider
         * @param request        HttpServletRequest
         * @return Thông báo xóa thành công
         * @throws AppException nếu không có quyền xóa, lịch hẹn không tồn tại hoặc đã
         *                      có appointment
         */
        @DeleteMapping("/provider/schedules/{scheduleId}")
        @PreAuthorize("hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<Void>> deleteSchedule(
                        @PathVariable String scheduleId,
                        Authentication authentication,
                        HttpServletRequest request) throws AppException {

                String providerId = authService.getUserIdFromAuthentication(authentication);
                providerScheduleService.deleteSchedule(providerId, scheduleId);

                return ResponseEntity.ok(ApiResponse.success(null, "Xóa lịch thành công", request));
        }

        /**
         * API lấy danh sách tất cả lịch hẹn của provider
         * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập
         * 
         * @param authentication Thông tin xác thực của provider
         * @param request        HttpServletRequest
         * @return Danh sách tất cả lịch hẹn của provider
         */
        @GetMapping("/provider/schedules")
        @PreAuthorize("hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<List<ProviderScheduleResponse>>> getProviderSchedules(
                        Authentication authentication,
                        HttpServletRequest request) throws AppException {

                String providerId = authService.getUserIdFromAuthentication(authentication);
                List<ProviderScheduleResponse> schedules = providerScheduleService.getProviderSchedules(providerId);

                return ResponseEntity.ok(ApiResponse.success(schedules, "Lấy danh sách lịch thành công", request));
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
        @GetMapping("/provider/schedules/paginated")
        @PreAuthorize("hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<Page<ProviderScheduleResponse>>> getProviderSchedulesPaginated(
                        Authentication authentication,
                        Pageable pageable,
                        HttpServletRequest request) throws AppException {

                String providerId = authService.getUserIdFromAuthentication(authentication);
                Page<ProviderScheduleResponse> schedules = providerScheduleService.getProviderSchedulesPaginated(
                                providerId,
                                pageable);

                return ResponseEntity
                                .ok(ApiResponse.success(schedules, "Lấy danh sách lịch có phân trang thành công",
                                                request));
        }

        /**
         * API tạo lịch làm việc cố định (recurring schedules)
         * Yêu cầu: Chỉ PROVIDER mới có quyền truy cập
         * 
         * @param createRequest  Thông tin tạo lịch lặp lại (serviceId, startDate,
         *                       endDate, daysOfWeek, shifts, maxCapacity, notes)
         * @param authentication Thông tin xác thực của provider
         * @param request        HttpServletRequest
         * @return Thông tin số lượng lịch đã được tạo
         * @throws AppException nếu service không thuộc về provider hoặc dữ liệu không
         *                      hợp lệ
         */
        @PostMapping("/provider/schedules/recurring")
        @PreAuthorize("hasRole('PROVIDER')")
        public ResponseEntity<ApiResponse<RecurringScheduleResponse>> createRecurringSchedules(
                        @Valid @RequestBody CreateRecurringScheduleRequest createRequest,
                        Authentication authentication,
                        HttpServletRequest request) throws AppException {

                String providerId = authService.getUserIdFromAuthentication(authentication);
                RecurringScheduleResponse response = providerScheduleService.createRecurringSchedules(providerId,
                                createRequest);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(response,
                                                "Đã tạo thành công " + response.getCreatedCount()
                                                                + " lịch làm việc cố định.",
                                                request));
        }
}
