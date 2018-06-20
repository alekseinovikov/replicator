package net.task.replicator.services;

import net.task.replicator.entities.TaskDefinition;
import net.task.replicator.entities.TaskDefinitionMirror;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Provides domain-specific functionality
 */
public interface TaskDefinitionService {
    @NotNull List<TaskDefinition> findAllNewTaskDefinitions();
    @NotNull List<TaskDefinitionMirror> findAllNewTaskDefinitionMirrors();
    @NotNull List<TaskDefinition> findAllTaskDefinitionsWithConflicts();
    void deleteAllTaskDefinitionsWithNoPair();
    void deleteAllTaskDefinitionMirrorsWithNoPair();
    void createPairFrom(@NotNull TaskDefinition taskDefinition);
    void createPairFrom(@NotNull TaskDefinitionMirror taskDefinitionMirror);
    void patch(@NotNull TaskDefinitionMirror mirror, @NotNull String name, String description, String syncUuid);
    void patch(@NotNull TaskDefinition taskDefinition, @NotNull String name, String description, String syncUuid);
}
