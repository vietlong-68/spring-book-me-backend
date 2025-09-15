package com.vietlong.spring_app.repository;

import com.vietlong.spring_app.model.Provider;
import com.vietlong.spring_app.model.ProviderStatus;
import com.vietlong.spring_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, String> {

    List<Provider> findByUserOrderByCreatedAtDesc(User user);

    List<Provider> findByStatusOrderByCreatedAtDesc(ProviderStatus status);

    Page<Provider> findByStatusOrderByCreatedAtDesc(ProviderStatus status, Pageable pageable);

    Page<Provider> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Provider> findByIsVerifiedTrueOrderByCreatedAtDesc();

    List<Provider> findByUserAndStatusOrderByCreatedAtDesc(User user, ProviderStatus status);

    boolean existsByUserAndStatus(User user, ProviderStatus status);

    long countByStatus(ProviderStatus status);

    long countByIsVerifiedTrue();

    @Query("SELECT p FROM Provider p WHERE p.businessName LIKE %:businessName% ORDER BY p.createdAt DESC")
    List<Provider> findByBusinessNameContaining(@Param("businessName") String businessName);

    @Query("SELECT p FROM Provider p WHERE p.address LIKE %:address% ORDER BY p.createdAt DESC")
    List<Provider> findByAddressContaining(@Param("address") String address);

    boolean existsByBusinessName(String businessName);
}
