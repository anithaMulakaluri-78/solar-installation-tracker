package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="installation_locations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallationLocation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="installation_id", nullable=false) private Installation installation;
    @Column(nullable=false, precision=10, scale=7) private BigDecimal latitude;
    @Column(nullable=false, precision=10, scale=7) private BigDecimal longitude;
    @Column(name="accuracy_meters", precision=10, scale=2) private BigDecimal accuracyMeters;
    @Column(name="captured_at") private LocalDateTime capturedAt;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="captured_by") private User capturedBy;
}
