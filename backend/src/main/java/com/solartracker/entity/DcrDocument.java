package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="dcr_documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DcrDocument {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="dcr_id", nullable=false) private DcrRecord dcr;
    @Column(name="document_type", nullable=false, length=50) private String documentType;
    @Column(name="file_name", length=255) private String fileName;
    @Column(name="file_path", length=500) private String filePath;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="uploaded_by") private User uploadedBy;
    @Column(name="uploaded_at") private LocalDateTime uploadedAt;
}
