package com.solartracker.repository;
import com.solartracker.entity.InstallationVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface InstallationVerificationRepository extends JpaRepository<InstallationVerification, Long> {
    Optional<InstallationVerification> findTopByInstallationIdOrderByVerifiedAtDesc(Long installationId);
}
