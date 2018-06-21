package net.task.replicator.services.impl;

import net.task.replicator.entities.TaskDefinition;
import net.task.replicator.entities.TaskDefinitionMirror;
import net.task.replicator.entities.base.BaseTaskDefinition;
import net.task.replicator.repositories.TaskDefinitionMirrorRepository;
import net.task.replicator.repositories.TaskDefinitionRepository;
import net.task.replicator.services.TaskDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * All the methods need an existing transaction
 */
@Service
public class DefaultTaskDefinitionService implements TaskDefinitionService {

    private final TaskDefinitionRepository taskDefinitionRepository;
    private final TaskDefinitionMirrorRepository taskDefinitionMirrorRepository;


    public DefaultTaskDefinitionService(@NotNull TaskDefinitionRepository taskDefinitionRepository,
                                        @NotNull TaskDefinitionMirrorRepository taskDefinitionMirrorRepository) {
        this.taskDefinitionRepository = taskDefinitionRepository;
        this.taskDefinitionMirrorRepository = taskDefinitionMirrorRepository;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @NotNull
    @Override
    public List<TaskDefinition> findAllNewTaskDefinitions() {
        return taskDefinitionRepository.findAllBySyncUuidIsNull();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @NotNull
    @Override
    public List<TaskDefinitionMirror> findAllNewTaskDefinitionMirrors() {
        return taskDefinitionMirrorRepository.findAllBySyncUuidIsNull();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public @NotNull List<TaskDefinition> findAllTaskDefinitionsWithConflicts() {
        return taskDefinitionRepository.findAllWithConflicts();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAllTaskDefinitionsWithNoPair() {
        taskDefinitionRepository.findAllByMirror_IdIsNullAndSyncUuidIsNotNull()
                .forEach(taskDefinitionRepository::delete);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAllTaskDefinitionMirrorsWithNoPair() {
        taskDefinitionMirrorRepository.findAllByTaskDefinition_IdIsNullAndSyncUuidIsNotNull()
                .forEach(taskDefinitionMirrorRepository::delete);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createPairFrom(@NotNull TaskDefinition taskDefinition) {
        taskDefinition = taskDefinitionRepository.saveAndFlush(taskDefinition);
        taskDefinitionMirrorRepository.saveAndFlush(TaskDefinitionMirror.from(taskDefinition));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createPairFrom(@NotNull TaskDefinitionMirror taskDefinitionMirror) {
        taskDefinitionMirror = taskDefinitionMirrorRepository.saveAndFlush(taskDefinitionMirror);
        taskDefinitionRepository.saveAndFlush(TaskDefinition.from(taskDefinitionMirror));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void patch(@NotNull TaskDefinitionMirror mirror, @NotNull String name, String description, String syncUuid) {
        patchBaseTask(mirror, name, description, syncUuid);
        taskDefinitionMirrorRepository.saveAndFlush(mirror);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void patch(@NotNull TaskDefinition taskDefinition, @NotNull String name, String description, String syncUuid) {
        patchBaseTask(taskDefinition, name, description, syncUuid);
        taskDefinitionRepository.saveAndFlush(taskDefinition);
    }

    private void patchBaseTask(BaseTaskDefinition baseTaskDefinition, @NotNull String name, String description, String syncUuid) {
        baseTaskDefinition.setName(name);
        baseTaskDefinition.setDescription(description);
        baseTaskDefinition.setSyncUuid(syncUuid);
    }

}
