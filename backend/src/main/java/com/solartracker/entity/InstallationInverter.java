package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="installation_inverters")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallationInverter {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="installation_id", nullable=false) private Installation installation;
    @Column(name="inverter_serial_number", nullable=false, length=100) private String inverterSerialNumber;
    @Column(name="inverter_make", length=100) private String inverterMake;
    @Column(name="inverter_model", length=100) private String inverterModel;
    @Column(name="inverter_capacity_kw", precision=10, scale=2) private BigDecimal inverterCapacityKw;
    @CreationTimestamp @Column(name="created_at", updatable=false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at") private LocalDateTime updatedAt;
}
