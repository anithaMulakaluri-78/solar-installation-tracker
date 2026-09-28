package com.solartracker.entity;

import com.solartracker.entity.enums.DcrStatus;
import com.solartracker.entity.enums.InstallationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "installations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "ULA-UO-APSPD-0126-0086100" - the business key from the DISCOM
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @Column(name = "ula_application_id", nullable = false, unique = true, length = 60)
    private String ulaApplicationId;

    @Column(length = 100)
    private String section;

    @Column(length = 150)
    private String distribution;

    @Column(name = "service_number", nullable = false, unique = true, length = 30)
    private String serviceNumber;

    @Column(name = "consumer_name", nullable = false, length = 150)
    private String consumerName;

    @Column(name = "consumer_mobile", length = 20)
    private String consumerMobile;

    @Column(name = "new_mobile", length = 20)
    private String newMobile;

    @Column(name = "inverter_serial_number", length = 100)
    private String inverterSerialNumber;

    @Column(name = "site_type", length = 50)
    private String siteType;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "dcr_status", length = 20)
    @Builder.Default
    private DcrStatus dcrStatus = DcrStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private InstallationStatus status = InstallationStatus.NEW;

    // Nullable until an installer is assigned to the job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_installer_id")
    private User assignedInstaller;

    @OneToOne(mappedBy = "installation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private InstallationInverter inverter;

    @OneToOne(mappedBy = "installation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private InstallationLocation location;

    @OneToMany(mappedBy = "installation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InstallationPanel> panels = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
