package com.gpn.loghistory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Getter
public class LogHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LogHistoryModule module;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected LogHistoryEntity() {
    }

    public LogHistoryEntity(final Long userId, final LogHistoryModule module, final Long objectId, final String action) {
        this.userId = userId;
        this.module = module;
        this.objectId = objectId;
        this.action = action;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
