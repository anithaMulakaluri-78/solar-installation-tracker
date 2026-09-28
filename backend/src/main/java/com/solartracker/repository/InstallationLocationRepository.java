package com.solartracker.repository;
import com.solartracker.entity.InstallationLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface InstallationLocationRepository extends JpaRepository<InstallationLocation, Long> {
    Optional<InstallationLocation> findByInstallationId(Long installationId);
}
