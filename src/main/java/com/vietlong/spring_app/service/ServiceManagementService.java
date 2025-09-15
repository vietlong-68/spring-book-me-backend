package com.vietlong.spring_app.service;

import com.vietlong.spring_app.dto.request.CreateServiceRequest;
import com.vietlong.spring_app.dto.request.UpdateServiceRequest;
import com.vietlong.spring_app.dto.response.ServiceResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.Category;
import com.vietlong.spring_app.model.Provider;
import com.vietlong.spring_app.model.ProviderStatus;
import com.vietlong.spring_app.model.Service;
import com.vietlong.spring_app.repository.CategoryRepository;
import com.vietlong.spring_app.repository.ProviderRepository;
import com.vietlong.spring_app.repository.ServiceRepository;
import com.vietlong.spring_app.repository.UserRepository;
import com.vietlong.spring_app.service.uploadfile.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceManagementService {

    private ServiceRepository serviceRepository;
    private CategoryRepository categoryRepository;
    private ProviderRepository providerRepository;
    private UserRepository userRepository;
    private FileUploadService fileUploadService;

    public ServiceManagementService(ServiceRepository serviceRepository,
            CategoryRepository categoryRepository,
            ProviderRepository providerRepository,
            UserRepository userRepository,
            FileUploadService fileUploadService) {
        this.serviceRepository = serviceRepository;
        this.categoryRepository = categoryRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
        this.fileUploadService = fileUploadService;
    }

    public ServiceResponse createService(String providerId, CreateServiceRequest request, MultipartFile imageFile,
            Authentication authentication)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (!category.isActive()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_ACTIVE);
        }

        String imageUrl = request.getImageUrl();

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageUrl = fileUploadService.uploadFile(imageFile);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload ảnh dịch vụ: " + e.getMessage());
            }
        }

        Service service = Service.builder()
                .provider(provider)
                .category(category)
                .serviceName(request.getServiceName())
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .durationMinutes(request.getDurationMinutes())
                .price(request.getPrice())
                .bufferTimeAfter(request.getBufferTimeAfter())
                .capacity(request.getCapacity())
                .isActive(true)
                .build();

        Service savedService = serviceRepository.save(service);
        return convertToServiceResponse(savedService);
    }

    public List<ServiceResponse> getProviderServices(String providerId, Authentication authentication)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        List<Service> services = serviceRepository.findByProviderOrderByCreatedAtDesc(provider);
        List<ServiceResponse> responseList = new ArrayList<>();
        for (Service service : services) {
            responseList.add(convertToServiceResponse(service));
        }
        return responseList;
    }

    public Page<ServiceResponse> getProviderServicesPaginated(String providerId, Authentication authentication,
            Pageable pageable)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Page<Service> services = serviceRepository.findByProviderOrderByCreatedAtDesc(provider, pageable);
        return services.map(this::convertToServiceResponse);
    }

    public ServiceResponse getServiceById(String providerId, String serviceId, Authentication authentication)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Service service = serviceRepository.findByIdAndProvider(serviceId, provider)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        return convertToServiceResponse(service);
    }

    @Transactional
    public ServiceResponse updateService(String providerId, String serviceId, UpdateServiceRequest request,
            MultipartFile imageFile,
            Authentication authentication)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Service service = serviceRepository.findByIdAndProvider(serviceId, provider)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        if (request.getServiceName() != null) {
            service.setServiceName(request.getServiceName());
        }
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            service.setImageUrl(request.getImageUrl());
        }
        if (request.getDurationMinutes() != null) {
            service.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }
        if (request.getBufferTimeAfter() != null) {
            service.setBufferTimeAfter(request.getBufferTimeAfter());
        }
        if (request.getCapacity() != null) {
            service.setCapacity(request.getCapacity());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            if (!category.isActive()) {
                throw new AppException(ErrorCode.CATEGORY_NOT_ACTIVE);
            }
            service.setCategory(category);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String newImageUrl = fileUploadService.uploadFile(imageFile);
                service.setImageUrl(newImageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload ảnh dịch vụ: " + e.getMessage());
            }
        }

        Service updatedService = serviceRepository.save(service);
        return convertToServiceResponse(updatedService);
    }

    @Transactional
    public ServiceResponse uploadServiceImage(String providerId, String serviceId, MultipartFile imageFile,
            Authentication authentication)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Service service = serviceRepository.findByIdAndProvider(serviceId, provider)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        try {
            String newImageUrl = fileUploadService.uploadFile(imageFile);
            service.setImageUrl(newImageUrl);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Lỗi khi upload ảnh dịch vụ: " + e.getMessage());
        }

        Service updatedService = serviceRepository.save(service);
        return convertToServiceResponse(updatedService);
    }

    @Transactional
    public void deleteService(String providerId, String serviceId, Authentication authentication) throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Service service = serviceRepository.findByIdAndProvider(serviceId, provider)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        serviceRepository.delete(service);
    }

    @Transactional
    public ServiceResponse activateService(String providerId, String serviceId, Authentication authentication)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Service service = serviceRepository.findByIdAndProvider(serviceId, provider)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        service.activate();
        Service updatedService = serviceRepository.save(service);
        return convertToServiceResponse(updatedService);
    }

    @Transactional
    public ServiceResponse deactivateService(String providerId, String serviceId, Authentication authentication)
            throws AppException {
        String userId = getUserIdFromAuthentication(authentication);
        Provider provider = getProviderByIdAndUserId(providerId, userId);

        Service service = serviceRepository.findByIdAndProvider(serviceId, provider)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        service.deactivate();
        Service updatedService = serviceRepository.save(service);
        return convertToServiceResponse(updatedService);
    }

    private String getUserIdFromAuthentication(Authentication authentication) throws AppException {
        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return authentication.getName();
    }

    private boolean isProviderActiveAndVerified(Provider provider) {
        return provider.getStatus() != null &&
                provider.getStatus().equals(ProviderStatus.ACTIVE) &&
                provider.getIsVerified() != null &&
                provider.getIsVerified();
    }

    private Provider getProviderByIdAndUserId(String providerId, String userId) throws AppException {

        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new AppException(ErrorCode.PROVIDER_NOT_FOUND));

        if (!provider.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.PROVIDER_NOT_FOUND);
        }

        if (!isProviderActiveAndVerified(provider)) {
            if (provider.getStatus() == null || !provider.getStatus().equals(ProviderStatus.ACTIVE)) {
                throw new AppException(ErrorCode.PROVIDER_NOT_ACTIVE);
            }
            if (provider.getIsVerified() == null || !provider.getIsVerified()) {
                throw new AppException(ErrorCode.PROVIDER_NOT_VERIFIED);
            }
        }

        return provider;
    }

    public List<ServiceResponse> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        List<ServiceResponse> responseList = new ArrayList<>();
        for (Service service : services) {
            responseList.add(convertToServiceResponse(service));
        }
        return responseList;
    }

    public Page<ServiceResponse> getAllServicesPaginated(Pageable pageable) {
        Page<Service> services = serviceRepository.findAll(pageable);
        return services.map(this::convertToServiceResponse);
    }

    public ServiceResponse getServiceByIdForAdmin(String serviceId) throws AppException {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));
        return convertToServiceResponse(service);
    }

    private ServiceResponse convertToServiceResponse(Service service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .description(service.getDescription())
                .imageUrl(service.getImageUrl())
                .durationMinutes(service.getDurationMinutes())
                .price(service.getPrice())
                .bufferTimeAfter(service.getBufferTimeAfter())
                .capacity(service.getCapacity())
                .isActive(service.getIsActive())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .providerId(service.getProvider().getId())
                .providerBusinessName(service.getProvider().getBusinessName())
                .categoryId(service.getCategory().getId())
                .categoryName(service.getCategory().getName())
                .totalDurationMinutes(service.getTotalDurationMinutes())
                .isGroupService(service.isGroupService())
                .isValidForBooking(service.isValidForBooking())
                .build();
    }
}