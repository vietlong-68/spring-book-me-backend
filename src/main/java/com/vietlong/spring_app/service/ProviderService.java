package com.vietlong.spring_app.service;

import com.vietlong.spring_app.dto.request.UpdateProviderRequest;
import com.vietlong.spring_app.dto.response.ProviderResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.Provider;
import com.vietlong.spring_app.model.ProviderStatus;
import com.vietlong.spring_app.model.User;
import com.vietlong.spring_app.repository.ProviderRepository;
import com.vietlong.spring_app.repository.UserRepository;
import com.vietlong.spring_app.service.uploadfile.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final FileUploadService fileUploadService;

    private User getCurrentUser(Authentication authentication) throws AppException {
        String userId = authService.getUserIdFromAuthentication(authentication);
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Không tìm thấy người dùng"));
    }

    public List<ProviderResponse> getMyProviders(Authentication authentication) throws AppException {
        User currentUser = getCurrentUser(authentication);

        List<Provider> providers = providerRepository.findByUserOrderByCreatedAtDesc(currentUser);
        java.util.List<ProviderResponse> result = new java.util.ArrayList<ProviderResponse>();
        for (Provider provider : providers) {
            result.add(ProviderResponse.fromEntity(provider));
        }
        return result;
    }

    public ProviderResponse getProviderById(String providerId) throws AppException {
        Provider provider = providerRepository.findById(providerId).get();
        if (provider == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy Provider");
        }
        return ProviderResponse.fromEntity(provider);
    }

    @Transactional
    public ProviderResponse updateProvider(String providerId,
            Authentication authentication,
            UpdateProviderRequest request,
            MultipartFile logoFile,
            MultipartFile bannerFile) throws AppException {
        User currentUser = getCurrentUser(authentication);
        Provider provider = null;
        java.util.Optional<Provider> optionalProvider = providerRepository.findById(providerId);
        if (optionalProvider.isPresent()) {
            provider = optionalProvider.get();
        } else {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy Provider");
        }

        if (provider.getUser() == null || provider.getUser().getId() == null
                || !provider.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED, "Bạn không có quyền sửa Provider này");
        }

        provider.setBusinessName(request.getBusinessName());
        provider.setBio(request.getBio());
        provider.setAddress(request.getAddress());
        provider.setPhoneNumber(request.getPhoneNumber());
        provider.setWebsiteUrl(request.getWebsite());

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                String newLogoUrl = fileUploadService.uploadFile(logoFile);
                provider.setLogoUrl(newLogoUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload logo: " + e.getMessage());
            }
        }

        if (bannerFile != null && !bannerFile.isEmpty()) {
            try {
                String newBannerUrl = fileUploadService.uploadFile(bannerFile);
                provider.setBannerUrl(newBannerUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload banner: " + e.getMessage());
            }
        }

        Provider savedProvider = providerRepository.save(provider);
        return ProviderResponse.fromEntity(savedProvider);
    }

    @Transactional
    public ProviderResponse updateProviderLogo(String providerId,
            Authentication authentication,
            MultipartFile logoFile) throws AppException {
        User currentUser = getCurrentUser(authentication);
        Provider provider = null;
        java.util.Optional<Provider> optionalProvider = providerRepository.findById(providerId);
        if (optionalProvider.isPresent()) {
            provider = optionalProvider.get();
        } else {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy Provider");
        }

        if (provider.getUser() == null || provider.getUser().getId() == null
                || !provider.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED, "Bạn không có quyền sửa Provider này");
        }

        try {
            String newLogoUrl = fileUploadService.uploadFile(logoFile);
            provider.setLogoUrl(newLogoUrl);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload logo: " + e.getMessage());
        }

        Provider savedProvider = providerRepository.save(provider);
        return ProviderResponse.fromEntity(savedProvider);
    }

    @Transactional
    public ProviderResponse updateProviderBanner(String providerId,
            Authentication authentication,
            MultipartFile bannerFile) throws AppException {
        User currentUser = getCurrentUser(authentication);
        Provider provider = null;
        java.util.Optional<Provider> optionalProvider = providerRepository.findById(providerId);
        if (optionalProvider.isPresent()) {
            provider = optionalProvider.get();
        } else {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Không tìm thấy Provider");
        }

        if (provider.getUser() == null || provider.getUser().getId() == null
                || !provider.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED, "Bạn không có quyền sửa Provider này");
        }

        try {
            String newBannerUrl = fileUploadService.uploadFile(bannerFile);
            provider.setBannerUrl(newBannerUrl);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload banner: " + e.getMessage());
        }

        Provider savedProvider = providerRepository.save(provider);
        return ProviderResponse.fromEntity(savedProvider);
    }

    public List<ProviderResponse> getAllProviders() {
        List<Provider> providers = providerRepository.findAll();
        List<ProviderResponse> responses = new java.util.ArrayList<ProviderResponse>();
        for (Provider provider : providers) {
            responses.add(ProviderResponse.fromEntity(provider));
        }
        return responses;
    }

    public List<ProviderResponse> getProvidersByStatus(com.vietlong.spring_app.model.ProviderStatus status) {
        List<Provider> providers = providerRepository.findByStatusOrderByCreatedAtDesc(status);
        List<ProviderResponse> responses = new java.util.ArrayList<ProviderResponse>();
        for (Provider provider : providers) {
            responses.add(ProviderResponse.fromEntity(provider));
        }
        return responses;
    }

    @Transactional
    public ProviderResponse updateProviderStatus(String providerId, ProviderStatus status) throws AppException {
        Provider provider = null;
        java.util.Optional<Provider> optionalProvider = providerRepository.findById(providerId);
        if (optionalProvider.isPresent()) {
            provider = optionalProvider.get();
        } else {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Không tìm thấy Provider");
        }

        provider.setStatus(status);
        Provider updatedProvider = providerRepository.save(provider);
        return ProviderResponse.fromEntity(updatedProvider);
    }
}
