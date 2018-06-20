package net.task.replicator.repositories;

import net.task.replicator.entities.TaskDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TaskDefinitionRepository extends JpaRepository<TaskDefinition, Integer> {

    /**
     * Finds all TaskDefinition that haven't been replicated yet
     *
     * @return list of entities for first replication
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NotNull List<TaskDefinition> findAllBySyncUuidIsNull();

    /**
     * Finds all TaskDefinition that don't have they pair but was replicated
     * It means that all pairs were deleted
     *
     * @return list of entities that pairs were deleted
     */
    //Can't put Lock here. H2 doesn't support it: Feature not supported: "MVCC=TRUE && FOR UPDATE && JOIN" :(
    @NotNull List<TaskDefinition> findAllByMirror_IdIsNullAndSyncUuidIsNotNull();

    /**
     * Finds all TaskDefinition that have changes or their pairs have, but have the same ID and syncUuid
     * It means that the pairs must be replicated
     *
     * @return list of entities with un-replicated changes
     */
    //Can't put Lock here. H2 doesn't support it: Feature not supported: "MVCC=TRUE && FOR UPDATE && JOIN" :(
    @Query("SELECT td FROM TaskDefinition as td JOIN td.mirror as m " +
            "WHERE m.syncUuid = td.syncUuid " +
            "   AND (td.name <> m.name " +
            "       OR ((td.description IS NULL AND m.description IS NOT NULL) " +
            "       OR (m.description IS NULL AND td.description IS NOT NULL) " +
            "       OR td.description <> m.description))")
    List<TaskDefinition> findAllWithConflicts();
}
