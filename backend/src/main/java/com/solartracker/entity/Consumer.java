package com.solartracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="consumers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consumer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="consumer_number", nullable=false, unique=true, length=60) private String consumerNumber;
    @Column(name="ula_application_id", unique=true, length=60) private String ulaApplicationId;
    @Column(nullable=false, length=150) private String name;
    @Column(length=20) private String mobile;
    @Column(name="alternate_mobile", length=20) private String alternateMobile;
    @Column(length=500) private String address;
    @Column(length=150) private String village;
    @Column(length=150) private String mandal;
    @Column(length=150) private String district;
    @Column(length=100) private String state;
    @Column(length=10) private String pincode;
    @Column(length=100) private String section;
    @Column(length=150) private String distribution;
    @Column(name="site_type", length=50) private String siteType;
    @Column(nullable=false, length=30) @Builder.Default private String status="ACTIVE";
    @CreationTimestamp @Column(name="created_at", updatable=false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name="updated_at") private LocalDateTime updatedAt;
}
