package net.task.replicator.entities;

import net.task.replicator.entities.base.BaseTaskDefinition;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * First table mapping
 * Used like a base entry (master table)
 */
@Entity
@Table(name = "task_definition")
public class TaskDefinition extends BaseTaskDefinition {

    @OneToOne
    @JoinColumn(name = "id")
    private TaskDefinitionMirror mirror;


    public TaskDefinition() {
    }

    public TaskDefinition(Integer id, @NotNull String name, String description, String syncUuid) {
        super(id, name, description, syncUuid);
    }

    public TaskDefinitionMirror getMirror() {
        return mirror;
    }

    public void setMirror(TaskDefinitionMirror mirror) {
        this.mirror = mirror;
    }

    public static TaskDefinition from(TaskDefinitionMirror taskDefinitionMirror) {
        if (null == taskDefinitionMirror) return null;

        return new TaskDefinition(taskDefinitionMirror.getId(),
                taskDefinitionMirror.getName(), taskDefinitionMirror.getDescription(), taskDefinitionMirror.getSyncUuid());
    }

}
