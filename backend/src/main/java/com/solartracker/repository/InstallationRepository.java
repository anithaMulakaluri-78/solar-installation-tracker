package com.solartracker.repository;

import com.solartracker.entity.Installation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InstallationRepository extends JpaRepository<Installation, Long>, JpaSpecificationExecutor<Installation> {
    boolean existsByUlaApplicationId(String ulaApplicationId);
    boolean existsByServiceNumber(String serviceNumber);
    boolean existsByUlaApplicationIdAndIdNot(String ulaApplicationId, Long id);
    boolean existsByServiceNumberAndIdNot(String serviceNumber, Long id);
}
