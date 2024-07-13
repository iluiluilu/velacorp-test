package com.linhnt.velaecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        Date now = getCurrentUtcDate();
        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = 0L;
        this.updatedBy = 0L;
        this.setDefaultDeleted();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = getCurrentUtcDate();
        this.setDefaultDeleted();
        this.updatedBy = 0L;
    }

    private Date getCurrentUtcDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendar.getTime();
    }

    private void setDefaultDeleted() {
        if (this.deleted == null) {
            this.deleted = false;
        }
    }
}
