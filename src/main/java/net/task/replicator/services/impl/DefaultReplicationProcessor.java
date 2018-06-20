package net.task.replicator.services.impl;

import net.task.replicator.entities.TaskDefinition;
import net.task.replicator.entities.TaskDefinitionMirror;
import net.task.replicator.services.ReplicationProcessor;
import net.task.replicator.services.TaskDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Is responsible for the replication logic
 * Determines the sequence and conditions of the process
 */
@Service
public class DefaultReplicationProcessor implements ReplicationProcessor {

    private final TaskDefinitionService taskDefinitionService;


    public DefaultReplicationProcessor(TaskDefinitionService taskDefinitionService) {
        this.taskDefinitionService = taskDefinitionService;
    }

    /**
     * All the sync must be in one transaction
     */
    @Transactional
    @Override
    public void startReplication() {
        //Create new entities
        replicateNewTaskDefinitions();
        replicateNewTaskDefinitionMirrors();

        //Delete all single entities
        replicateDeletedTaskDefinitionMirrors();
        replicateDeletedTaskDefinitions();

        //Merge changes
        replicateChanges();
    }

    /**
     * Finds and replicate new entities
     */
    private void replicateNewTaskDefinitionMirrors() {
        List<TaskDefinitionMirror> mirrors = taskDefinitionService.findAllNewTaskDefinitionMirrors();
        mirrors.forEach(mirror -> {
            if (null == mirror.getTaskDefinition()) {
                //If we don't have pair with the same ID - create one
                taskDefinitionService.createPairFrom(mirror);
            } else {
                //Else, the last is a winner
                updateExistingPairByUpdateAt(mirror.getTaskDefinition(), mirror);
            }
        });
    }

    /**
     * Finds and replicate new entities
     */
    private void replicateNewTaskDefinitions() {
        List<TaskDefinition> newTaskDefinitions = taskDefinitionService.findAllNewTaskDefinitions();
        newTaskDefinitions.forEach(taskDefinition -> {
            if (null == taskDefinition.getMirror()) {
                //If we don't have pair with the same ID - create one
                taskDefinitionService.createPairFrom(taskDefinition);
            } else {
                //Else, the last is a winner
                updateExistingPairByUpdateAt(taskDefinition, taskDefinition.getMirror());
            }
        });
    }

    /**
     * Finds all pairs with conflicts (changes) and updates it
     */
    private void replicateChanges() {
        List<TaskDefinition> taskDefinitions = taskDefinitionService.findAllTaskDefinitionsWithConflicts();
        taskDefinitions.forEach(taskDefinition -> updateExistingPairByUpdateAt(taskDefinition, taskDefinition.getMirror()));
    }

    /**
     * Takes entity with its mirror and determine whats changes was the last to apply them
     *
     * @param taskDefinition - main entity
     * @param mirror - its mirror
     */
    private void updateExistingPairByUpdateAt(@NotNull TaskDefinition taskDefinition,
                                              @NotNull TaskDefinitionMirror mirror) {
        if (mirror.getUpdatedAt().before(taskDefinition.getUpdatedAt())) {
            taskDefinitionService.patch(mirror,
                    taskDefinition.getName(), taskDefinition.getDescription(), taskDefinition.getSyncUuid());
            return;
        }
        taskDefinitionService.patch(taskDefinition, mirror.getName(), mirror.getDescription(), mirror.getSyncUuid());
    }

    private void replicateDeletedTaskDefinitionMirrors() {
        taskDefinitionService.deleteAllTaskDefinitionsWithNoPair();
    }

    private void replicateDeletedTaskDefinitions() {
        taskDefinitionService.deleteAllTaskDefinitionMirrorsWithNoPair();
    }

}
