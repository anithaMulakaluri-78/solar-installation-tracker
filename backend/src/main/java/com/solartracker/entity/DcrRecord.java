package com.solartracker.entity;

import com.solartracker.entity.enums.DcrStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="dcr_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DcrRecord {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="installation_id", nullable=false) private Installation installation;
    @Column(name="dcr_number", length=100) private String dcrNumber;
    @Enumerated(EnumType.STRING) @Column(name="dcr_status", nullable=false, length=30) @Builder.Default private DcrStatus dcrStatus=DcrStatus.PENDING;
    @Column(name="submitted_at") private LocalDateTime submittedAt;
    @Column(name="verified_at") private LocalDateTime verifiedAt;
    @Column(columnDefinition="TEXT") private String remarks;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
}
