package net.task.replicator.entities.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Contains common fields
 */
@MappedSuperclass
public abstract class BaseTaskDefinition extends BaseSyncEntity {

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;


    public BaseTaskDefinition() {
    }

    public BaseTaskDefinition(Integer id,
                              @NotNull String name,
                              String description,
                              String syncUuid) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setSyncUuid(syncUuid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseTaskDefinition that = (BaseTaskDefinition) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, description);
    }

    @Override
    public String toString() {
        return "BaseTaskDefinition{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
