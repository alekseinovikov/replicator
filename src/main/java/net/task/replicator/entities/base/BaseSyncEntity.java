package net.task.replicator.entities.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents super class that could be replicated
 */
@MappedSuperclass
public abstract class BaseSyncEntity extends BaseEntity {

    //No need to insert, updates on DB
    @Column(name = "updatedAt", insertable = false)
    private Timestamp updatedAt;

    @Column(name = "sync_uuid")
    private String syncUuid;


    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSyncUuid() {
        return syncUuid;
    }

    public void setSyncUuid(String syncUuid) {
        this.syncUuid = syncUuid;
    }

    @PrePersist
    @PreUpdate
    public void updateSyncUuid() {
        //Set by the system to ensure that the entity was already replicated
        if (null == syncUuid) {
            syncUuid = UUID.randomUUID().toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseSyncEntity that = (BaseSyncEntity) o;
        return Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(syncUuid, that.syncUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), updatedAt, syncUuid);
    }

    @Override
    public String toString() {
        return "BaseSyncEntity{" +
                "updatedAt=" + updatedAt +
                ", syncUuid='" + syncUuid + '\'' +
                '}';
    }

}
