package com.solartracker.repository;
import com.solartracker.entity.InstallationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface InstallationPhotoRepository extends JpaRepository<InstallationPhoto, Long> {
    List<InstallationPhoto> findByInstallationIdOrderByCapturedAtDesc(Long installationId);
}
