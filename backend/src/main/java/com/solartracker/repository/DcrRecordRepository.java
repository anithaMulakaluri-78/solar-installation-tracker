package com.solartracker.repository;
import com.solartracker.entity.DcrRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DcrRecordRepository extends JpaRepository<DcrRecord, Long> {
    Optional<DcrRecord> findByInstallationId(Long installationId);
}
