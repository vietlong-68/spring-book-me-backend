package com.vietlong.spring_app.service;

import com.vietlong.spring_app.dto.request.CreateProviderApplicationRequest;
import com.vietlong.spring_app.dto.request.ReviewProviderApplicationRequest;
import com.vietlong.spring_app.dto.request.UpdateProviderApplicationRequest;
import com.vietlong.spring_app.dto.response.ProviderApplicationResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.Provider;
import com.vietlong.spring_app.model.ProviderApplication;
import com.vietlong.spring_app.model.ProviderApplicationStatus;
import com.vietlong.spring_app.model.ProviderStatus;
import com.vietlong.spring_app.model.User;
import com.vietlong.spring_app.repository.ProviderApplicationRepository;
import com.vietlong.spring_app.repository.ProviderRepository;
import com.vietlong.spring_app.repository.UserRepository;
import com.vietlong.spring_app.service.uploadfile.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderApplicationService {

    private final ProviderApplicationRepository providerApplicationRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final FileUploadService fileUploadService;
    private final EmailService emailService;
    private final AuthService authService;

    private User getCurrentUser(Authentication authentication) throws AppException {
        String userId = authService.getUserIdFromAuthentication(authentication);
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy người dùng"));
    }

    @Transactional
    public ProviderApplicationResponse createApplication(Authentication authentication,
            CreateProviderApplicationRequest request,
            MultipartFile businessLicenseFile) throws AppException {
        User currentUser = getCurrentUser(authentication);

        if (providerApplicationRepository.existsByUserAndStatus(currentUser, ProviderApplicationStatus.PENDING)) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Bạn đã có đơn đăng ký đang chờ duyệt");
        }

        String businessLicenseFileUrl;
        try {
            businessLicenseFileUrl = fileUploadService.uploadFile(businessLicenseFile);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED,
                    "Lỗi khi upload giấy phép kinh doanh: " + e.getMessage());
        }

        ProviderApplication application = ProviderApplication.builder()
                .user(currentUser)
                .businessName(request.getBusinessName())
                .bio(request.getBio())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .website(request.getWebsite())
                .businessLicenseFileUrl(businessLicenseFileUrl)
                .status(ProviderApplicationStatus.PENDING)
                .build();

        ProviderApplication savedApplication = providerApplicationRepository.save(application);
        return ProviderApplicationResponse.fromEntity(savedApplication);
    }

    public List<ProviderApplicationResponse> getUserApplications(Authentication authentication) throws AppException {
        User currentUser = getCurrentUser(authentication);
        List<ProviderApplication> applications = providerApplicationRepository
                .findByUserOrderByCreatedAtDesc(currentUser);

        List<ProviderApplicationResponse> responseList = new ArrayList<ProviderApplicationResponse>();
        for (ProviderApplication application : applications) {
            responseList.add(ProviderApplicationResponse.fromEntity(application));
        }
        return responseList;
    }

    public ProviderApplicationResponse getApplicationById(String applicationId, Authentication authentication)
            throws AppException {
        User currentUser = getCurrentUser(authentication);
        ProviderApplication application = providerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy đơn đăng ký"));

        if (!application.getUser().getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new AppException(ErrorCode.ACCESS_DENIED, "Bạn không có quyền xem đơn đăng ký này");
        }

        return ProviderApplicationResponse.fromEntity(application);
    }

    @Transactional
    public ProviderApplicationResponse updateApplication(String applicationId,
            Authentication authentication,
            UpdateProviderApplicationRequest request,
            MultipartFile businessLicenseFile) throws AppException {
        User currentUser = getCurrentUser(authentication);
        ProviderApplication application = providerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy đơn đăng ký"));

        if (!application.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED, "Bạn không có quyền sửa đơn đăng ký này");
        }

        if (!application.canBeEdited()) {
            throw new AppException(ErrorCode.INVALID_REQUEST,
                    "Không thể sửa đơn đăng ký với trạng thái: " + application.getStatus().getDisplayName());
        }

        application.setBusinessName(request.getBusinessName());
        application.setBio(request.getBio());
        application.setAddress(request.getAddress());
        application.setPhoneNumber(request.getPhoneNumber());
        application.setWebsite(request.getWebsite());

        if (businessLicenseFile != null && !businessLicenseFile.isEmpty()) {
            try {
                String newBusinessLicenseFileUrl = fileUploadService.uploadFile(businessLicenseFile);
                application.setBusinessLicenseFileUrl(newBusinessLicenseFileUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED,
                        "Lỗi khi upload giấy phép kinh doanh: " + e.getMessage());
            }
        }

        ProviderApplication savedApplication = providerApplicationRepository.save(application);
        return ProviderApplicationResponse.fromEntity(savedApplication);
    }

    @Transactional
    public void cancelApplication(String applicationId, Authentication authentication) throws AppException {
        User currentUser = getCurrentUser(authentication);
        ProviderApplication application = providerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy đơn đăng ký"));

        if (!application.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED, "Bạn không có quyền hủy đơn đăng ký này");
        }

        if (!application.canBeCancelled()) {
            throw new AppException(ErrorCode.INVALID_REQUEST,
                    "Không thể hủy đơn đăng ký với trạng thái: " + application.getStatus().getDisplayName());
        }

        application.cancel();
        providerApplicationRepository.save(application);
    }

    public Page<ProviderApplicationResponse> getAllApplications(Pageable pageable) {
        Page<ProviderApplication> applications = providerApplicationRepository.findAllByOrderByCreatedAtDesc(pageable);
        return applications.map(ProviderApplicationResponse::fromEntity);
    }

    public Page<ProviderApplicationResponse> getApplicationsByStatus(ProviderApplicationStatus status,
            Pageable pageable) {
        Page<ProviderApplication> applications = providerApplicationRepository.findByStatusOrderByCreatedAtDesc(status,
                pageable);
        return applications.map(ProviderApplicationResponse::fromEntity);
    }

    @Transactional
    public ProviderApplicationResponse approveApplication(String applicationId, Authentication authentication)
            throws AppException {
        User admin = getCurrentUser(authentication);
        ProviderApplication application = providerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy đơn đăng ký"));

        if (!application.canBeReviewed()) {
            throw new AppException(ErrorCode.INVALID_REQUEST,
                    "Không thể duyệt đơn đăng ký với trạng thái: " + application.getStatus().getDisplayName());
        }

        application.approve(admin);
        providerApplicationRepository.save(application);

        Provider provider = Provider.builder()
                .user(application.getUser())
                .businessName(application.getBusinessName())
                .bio(application.getBio())
                .address(application.getAddress())
                .phoneNumber(application.getPhoneNumber())
                .websiteUrl(application.getWebsite())
                .logoUrl(null)
                .bannerUrl(null)
                .status(ProviderStatus.ACTIVE)
                .isVerified(true)
                .build();

        providerRepository.save(provider);

        User user = application.getUser();
        user.setRole(com.vietlong.spring_app.model.Role.PROVIDER);
        userRepository.save(user);

        application.complete();
        providerApplicationRepository.save(application);

        try {
            String subject = "Đơn đăng ký Provider đã được duyệt";
            String content = String.format(
                    "Xin chào %s,\n\n" +
                            "Chúc mừng! Đơn đăng ký Provider của bạn đã được duyệt.\n" +
                            "Bạn có thể bắt đầu cung cấp dịch vụ trên nền tảng Book Me.\n\n" +
                            "Trân trọng,\n" +
                            "Đội ngũ Book Me",
                    user.getDisplayName());
            emailService.sendEmail(user.getEmail(), subject, content);
        } catch (Exception e) {

            System.err.println("Failed to send approval email: " + e.getMessage());
        }

        return ProviderApplicationResponse.fromEntity(application);
    }

    @Transactional
    public ProviderApplicationResponse rejectApplication(String applicationId,
            Authentication authentication,
            ReviewProviderApplicationRequest request) throws AppException {
        User admin = getCurrentUser(authentication);
        ProviderApplication application = providerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy đơn đăng ký"));

        if (!application.canBeReviewed()) {
            throw new AppException(ErrorCode.INVALID_REQUEST,
                    "Không thể từ chối đơn đăng ký với trạng thái: " + application.getStatus().getDisplayName());
        }

        application.reject(admin, request.getRejectionReason());
        providerApplicationRepository.save(application);

        try {
            String subject = "Đơn đăng ký Provider đã bị từ chối";
            String content = String.format(
                    "Xin chào %s,\n\n" +
                            "Rất tiếc, đơn đăng ký Provider của bạn đã bị từ chối.\n" +
                            "Lý do: %s\n\n" +
                            "Bạn có thể nộp đơn đăng ký mới sau khi khắc phục các vấn đề trên.\n\n" +
                            "Trân trọng,\n" +
                            "Đội ngũ Book Me",
                    application.getUser().getDisplayName(),
                    request.getRejectionReason() != null ? request.getRejectionReason() : "Không có lý do cụ thể");
            emailService.sendEmail(application.getUser().getEmail(), subject, content);
        } catch (Exception e) {

            System.err.println("Failed to send rejection email: " + e.getMessage());
        }

        return ProviderApplicationResponse.fromEntity(application);
    }
}
