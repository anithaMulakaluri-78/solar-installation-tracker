package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="installation_photos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallationPhoto {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="installation_id", nullable=false) private Installation installation;
    @Column(name="photo_type", nullable=false, length=50) private String photoType;
    @Column(name="file_name", length=255) private String fileName;
    @Column(name="file_path", length=500) private String filePath;
    @Column(precision=10, scale=7) private BigDecimal latitude;
    @Column(precision=10, scale=7) private BigDecimal longitude;
    @Column(name="captured_at") private LocalDateTime capturedAt;
    @Column(name="uploaded_at") private LocalDateTime uploadedAt;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="captured_by") private User capturedBy;
    @Column(length=30) @Builder.Default private String status="PENDING";
}
