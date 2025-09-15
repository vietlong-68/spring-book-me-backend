package com.vietlong.spring_app.repository;

import com.vietlong.spring_app.model.Category;
import com.vietlong.spring_app.model.Provider;
import com.vietlong.spring_app.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {

        List<Service> findByProviderOrderByCreatedAtDesc(Provider provider);

        List<Service> findByProviderAndIsActiveOrderByCreatedAtDesc(Provider provider, Boolean isActive);

        Page<Service> findByProviderOrderByCreatedAtDesc(Provider provider, Pageable pageable);

        Page<Service> findByProviderAndIsActiveOrderByCreatedAtDesc(Provider provider, Boolean isActive,
                        Pageable pageable);

        Optional<Service> findByIdAndProvider(String id, Provider provider);

        boolean existsByIdAndProvider(String id, Provider provider);

        long countByProvider(Provider provider);

        long countByProviderAndIsActive(Provider provider, Boolean isActive);

        @Query("SELECT s FROM Service s WHERE s.provider = :provider AND s.serviceName LIKE %:serviceName% ORDER BY s.createdAt DESC")
        List<Service> findByProviderAndServiceNameContaining(@Param("provider") Provider provider,
                        @Param("serviceName") String serviceName);

        @Query("SELECT s FROM Service s WHERE s.provider = :provider AND s.isActive = :isActive AND s.serviceName LIKE %:serviceName% ORDER BY s.createdAt DESC")
        List<Service> findByProviderAndIsActiveAndServiceNameContaining(@Param("provider") Provider provider,
                        @Param("isActive") Boolean isActive, @Param("serviceName") String serviceName);

        List<Service> findByCategory(Category category);

        long countByCategory(Category category);

        boolean existsByCategory(Category category);

        Page<Service> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

        Optional<Service> findByIdAndIsActiveTrue(String id);

        Page<Service> findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(Category category, Pageable pageable);
}
