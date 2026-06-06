package com.job.jobalerts.preferences.entity;

import com.job.jobalerts.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_preferences")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "location")
    private String location;

    @Column(name = "minimum_package")
    private Double minimumPackage;

    @Column(name = "active")
    private boolean active;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}