package net.task.replicator.repositories;

import net.task.replicator.entities.TaskDefinitionMirror;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TaskDefinitionMirrorRepository extends JpaRepository<TaskDefinitionMirror, Integer> {

    /**
     * Finds all TaskDefinitionMirror that haven't been replicated yet
     *
     * @return list of entities for first replication
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NotNull List<TaskDefinitionMirror> findAllBySyncUuidIsNull();

    /**
     * Finds all TaskDefinitionMirror that don't have they pair but was replicated
     * It means that all pairs were deleted
     *
     * @return list of entities that pairs were deleted
     */
    //Can't put Lock here. H2 doesn't support it: Feature not supported: "MVCC=TRUE && FOR UPDATE && JOIN" :(
    @NotNull List<TaskDefinitionMirror> findAllByTaskDefinition_IdIsNullAndSyncUuidIsNotNull();
}
