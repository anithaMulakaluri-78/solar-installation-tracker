package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="installation_sync_queue")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallationSyncQueue {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="installation_id", nullable=false) private Installation installation;
    @Column(name="operation_type", nullable=false, length=30) private String operationType;
    @Column(name="entity_type", nullable=false, length=50) private String entityType;
    @Column(name="entity_id") private Long entityId;
    @Column(columnDefinition="JSON") private String payload;
    @Column(name="sync_status", nullable=false, length=30) @Builder.Default private String syncStatus="PENDING";
    @Column(name="retry_count") @Builder.Default private Integer retryCount=0;
    @Column(name="last_error", columnDefinition="TEXT") private String lastError;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="synced_at") private LocalDateTime syncedAt;
}
