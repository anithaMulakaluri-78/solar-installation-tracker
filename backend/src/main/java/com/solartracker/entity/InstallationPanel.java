package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "installation_panels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstallationPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "installation_id", nullable = false)
    private Installation installation;

    // 1, 2, 3, 4... matches the sheet's Panel-1/Panel-2/Panel-3/Panel-4 columns,
    // but as rows instead of fixed columns so a site isn't capped at 4 panels.
    @Column(name = "panel_position")
    private Integer panelPosition;

    @Column(name = "panel_serial_number", nullable = false, length = 100)
    private String panelSerialNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
