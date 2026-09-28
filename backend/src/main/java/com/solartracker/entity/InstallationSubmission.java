package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="installation_submissions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallationSubmission {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="installation_id", nullable=false) private Installation installation;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="submitted_by", nullable=false) private User submittedBy;
    @Column(name="submission_status", nullable=false, length=30) private String submissionStatus;
    @Column(columnDefinition="TEXT") private String remarks;
    @Column(name="submitted_at") private LocalDateTime submittedAt;
}
