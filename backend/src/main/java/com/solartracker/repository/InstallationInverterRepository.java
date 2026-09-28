package com.solartracker.repository;
import com.solartracker.entity.InstallationInverter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface InstallationInverterRepository extends JpaRepository<InstallationInverter, Long> {
    Optional<InstallationInverter> findByInstallationId(Long installationId);
    boolean existsByInverterSerialNumber(String serial);
}
