package com.vietlong.spring_app.repository;

import com.vietlong.spring_app.model.ProviderApplication;
import com.vietlong.spring_app.model.ProviderApplicationStatus;
import com.vietlong.spring_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderApplicationRepository extends JpaRepository<ProviderApplication, String> {

    List<ProviderApplication> findByUserOrderByCreatedAtDesc(User user);

    Optional<ProviderApplication> findByUserAndStatus(User user, ProviderApplicationStatus status);

    boolean existsByUserAndStatus(User user, ProviderApplicationStatus status);

    Page<ProviderApplication> findByStatusOrderByCreatedAtDesc(ProviderApplicationStatus status, Pageable pageable);

    Page<ProviderApplication> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<ProviderApplication> findByReviewedByOrderByReviewedAtDesc(User reviewer);

    long countByStatus(ProviderApplicationStatus status);

    List<ProviderApplication> findByUserAndStatusOrderByCreatedAtDesc(User user, ProviderApplicationStatus status);

    @Query("SELECT pa FROM ProviderApplication pa WHERE pa.status = :status ORDER BY pa.submittedAt ASC")
    List<ProviderApplication> findPendingApplicationsForReview(@Param("status") ProviderApplicationStatus status);

    @Query("SELECT pa FROM ProviderApplication pa WHERE pa.user = :user AND pa.status = :status ORDER BY pa.createdAt DESC")
    Optional<ProviderApplication> findLatestApplicationByUserAndStatus(@Param("user") User user,
            @Param("status") ProviderApplicationStatus status);
}
