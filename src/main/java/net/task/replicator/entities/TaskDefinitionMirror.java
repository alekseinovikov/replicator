package net.task.replicator.entities;

import net.task.replicator.entities.base.BaseTaskDefinition;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Second table mapping
 * Used like a secondary table (slave)
 */
@Entity
@Table(name = "task_definition_mirror")
public class TaskDefinitionMirror extends BaseTaskDefinition {

    @OneToOne
    @JoinColumn(name = "id")
    private TaskDefinition taskDefinition;


    public TaskDefinitionMirror() {
    }

    public TaskDefinitionMirror(Integer id, @NotNull String name, String description, String syncUuid) {
        super(id, name, description, syncUuid);
    }

    public TaskDefinition getTaskDefinition() {
        return taskDefinition;
    }

    public void setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }

    public static TaskDefinitionMirror from(TaskDefinition taskDefinition) {
        if (null == taskDefinition) return null;

        return new TaskDefinitionMirror(taskDefinition.getId(),
                taskDefinition.getName(), taskDefinition.getDescription(), taskDefinition.getSyncUuid());
    }

}
