package com.solartracker.repository;
import com.solartracker.entity.InstallationSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface InstallationSubmissionRepository extends JpaRepository<InstallationSubmission, Long> {
    List<InstallationSubmission> findByInstallationIdOrderBySubmittedAtDesc(Long installationId);
}
