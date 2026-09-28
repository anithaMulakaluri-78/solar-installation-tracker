package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="installation_verifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallationVerification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="installation_id", nullable=false) private Installation installation;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="verified_by", nullable=false) private User verifiedBy;
    @Column(name="verification_status", nullable=false, length=30) private String verificationStatus;
    @Column(name="verification_remarks", columnDefinition="TEXT") private String verificationRemarks;
    @Column(name="verified_at") private LocalDateTime verifiedAt;
}
