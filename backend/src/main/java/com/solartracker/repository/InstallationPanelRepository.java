package com.solartracker.repository;

import com.solartracker.entity.InstallationPanel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstallationPanelRepository extends JpaRepository<InstallationPanel, Long> {
    boolean existsByPanelSerialNumber(String panelSerialNumber);
    Optional<InstallationPanel> findByPanelSerialNumber(String panelSerialNumber);
}
