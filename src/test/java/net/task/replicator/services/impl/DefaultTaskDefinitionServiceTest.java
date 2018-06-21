package net.task.replicator.services.impl;

import net.task.replicator.entities.TaskDefinition;
import net.task.replicator.entities.TaskDefinitionMirror;
import net.task.replicator.repositories.TaskDefinitionMirrorRepository;
import net.task.replicator.repositories.TaskDefinitionRepository;
import net.task.replicator.services.TaskDefinitionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class DefaultTaskDefinitionServiceTest {

    private TaskDefinitionService taskDefinitionService;

    @Autowired
    private TaskDefinitionRepository taskDefinitionRepository;

    @Autowired
    private TaskDefinitionMirrorRepository taskDefinitionMirrorRepository;


    @Autowired
    private EntityManager entityManager;


    @Before
    public void init() {
        taskDefinitionService = new DefaultTaskDefinitionService(taskDefinitionRepository, taskDefinitionMirrorRepository);
    }


    @Test
    public void findAllNewTaskDefinitions_dbIsEmpty_emptyList() {
        //act
        @NotNull List<TaskDefinition> allNewTaskDefinitions = taskDefinitionService.findAllNewTaskDefinitions();

        //assert
        assertNotNull(allNewTaskDefinitions);
        assertEquals(0, allNewTaskDefinitions.size());
    }

    @Test
    public void findAllNewTaskDefinitions_dbHas2RowsWithNoSyncUuid_listContains2Rows() {
        //arrange
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description, sync_uuid) VALUES(3, 'name3', 'description2', 'test_uuid')").executeUpdate();

        //act
        @NotNull List<TaskDefinition> allNewTaskDefinitions = taskDefinitionService.findAllNewTaskDefinitions();

        //assert
        assertNotNull(allNewTaskDefinitions);
        assertEquals(2, allNewTaskDefinitions.size());
        assertTrue(allNewTaskDefinitions.contains(new TaskDefinition(1, "name1", "description2", null)));
        assertTrue(allNewTaskDefinitions.contains(new TaskDefinition(2, "name2", null, null)));
    }

    @Test
    public void findAllNewTaskDefinitionMirrors_dbIsEmpty_emptyList() {
        //act
        @NotNull List<TaskDefinitionMirror> allNewTaskDefinitionMirrors = taskDefinitionService.findAllNewTaskDefinitionMirrors();

        //assert
        assertNotNull(allNewTaskDefinitionMirrors);
        assertEquals(0, allNewTaskDefinitionMirrors.size());
    }

    @Test
    public void findAllNewTaskDefinitionMirrors_dbHas2RowsWithNoSyncUuid_listContains2Rows() {
        //arrange
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description, sync_uuid) VALUES(3, 'name3', 'description2', 'test_uuid')").executeUpdate();

        //act
        @NotNull List<TaskDefinitionMirror> allNewTaskDefinitionMirrors = taskDefinitionService.findAllNewTaskDefinitionMirrors();

        //assert
        assertNotNull(allNewTaskDefinitionMirrors);
        assertEquals(2, allNewTaskDefinitionMirrors.size());
        assertTrue(allNewTaskDefinitionMirrors.contains(new TaskDefinitionMirror(1, "name1", "description2", null)));
        assertTrue(allNewTaskDefinitionMirrors.contains(new TaskDefinitionMirror(2, "name2", null, null)));
    }

    @Test
    public void findAllTaskDefinitionsWithConflicts_dbIsEmpty_emptyList() {
        //act
        @NotNull List<TaskDefinition> allTaskDefinitionsWithConflicts = taskDefinitionService.findAllTaskDefinitionsWithConflicts();

        //assert
        assertNotNull(allTaskDefinitionsWithConflicts);
        assertEquals(0, allTaskDefinitionsWithConflicts.size());
    }

    @Test
    public void findAllTaskDefinitionsWithConflicts_dbHasNoConflictsButRows_emptyList() {
        //arrange
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description, sync_uuid) VALUES(3, 'name3', 'description2', 'test_uuid')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description, sync_uuid) VALUES(4, 'name4', 'description4', 'test_uuid')").executeUpdate();

        //act
        @NotNull List<TaskDefinition> allTaskDefinitionsWithConflicts = taskDefinitionService.findAllTaskDefinitionsWithConflicts();

        //assert
        assertNotNull(allTaskDefinitionsWithConflicts);
        assertEquals(0, allTaskDefinitionsWithConflicts.size());
    }

    @Test
    public void findAllTaskDefinitionsWithConflicts_dbHasOneConflict_listHasOneRow() {
        //arrange
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description, sync_uuid) VALUES(3, 'name3', 'description2', 'test_uuid')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description, sync_uuid) VALUES(3, 'name4', 'description4', 'test_uuid')").executeUpdate();

        //act
        @NotNull List<TaskDefinition> allTaskDefinitionsWithConflicts = taskDefinitionService.findAllTaskDefinitionsWithConflicts();

        //assert
        assertNotNull(allTaskDefinitionsWithConflicts);
        assertEquals(1, allTaskDefinitionsWithConflicts.size());
        assertTrue(allTaskDefinitionsWithConflicts.contains(new TaskDefinition(3, "name4", "description4", "test_uuid")));
    }

    @Test
    public void deleteAllTaskDefinitionsWithNoPair_hasOne_deleted() {
        //arrange
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description, sync_uuid) VALUES(3, 'name3', 'description3', 'test_uuid')").executeUpdate();

        //act
        taskDefinitionService.deleteAllTaskDefinitionsWithNoPair();

        //assert
        assertNull(entityManager.find(TaskDefinition.class, 3));
    }

    @Test
    public void deleteAllTaskDefinitionMirrorsWithNoPair_hasOne_deleted() {
        //arrange
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description, sync_uuid) VALUES(3, 'name3', 'description3', 'test_uuid')").executeUpdate();

        //act
        taskDefinitionService.deleteAllTaskDefinitionMirrorsWithNoPair();

        //assert
        assertNull(entityManager.find(TaskDefinitionMirror.class, 3));
    }

    @Test
    public void createPairFrom_oneNewTaskDefinition_createsAPair() {
        //arrange
        TaskDefinition taskDefinition = new TaskDefinition(1, "name", "descr", null);

        //act
        taskDefinitionService.createPairFrom(taskDefinition);

        //arrange
        taskDefinition = entityManager.find(TaskDefinition.class, 1);
        TaskDefinitionMirror taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 1);

        assertNotNull(taskDefinitionMirror);
        assertEquals(taskDefinition.getId(), taskDefinitionMirror.getId());
        assertEquals(taskDefinition.getName(), taskDefinitionMirror.getName());
        assertEquals(taskDefinition.getDescription(), taskDefinitionMirror.getDescription());
        assertEquals(taskDefinition.getSyncUuid(), taskDefinitionMirror.getSyncUuid());
    }

    @Test
    public void createPairFrom_oneNewTaskDefinitionMirror_createsAPair() {
        //arrange
        TaskDefinitionMirror taskDefinitionMirror = new TaskDefinitionMirror(1, "name", "descr", null);

        //act
        taskDefinitionService.createPairFrom(taskDefinitionMirror);

        //arrange
        taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 1);
        TaskDefinition taskDefinition = entityManager.find(TaskDefinition.class, 1);

        assertNotNull(taskDefinition);
        assertEquals(taskDefinitionMirror.getId(), taskDefinition.getId());
        assertEquals(taskDefinitionMirror.getName(), taskDefinition.getName());
        assertEquals(taskDefinitionMirror.getDescription(), taskDefinition.getDescription());
        assertEquals(taskDefinitionMirror.getSyncUuid(), taskDefinition.getSyncUuid());
    }

    @Test
    public void patch_TaskDefinitionChanges_applied() {
        //arrange
        TaskDefinition taskDefinition = new TaskDefinition(1, "name", null, null);

        //act
        taskDefinitionService.patch(taskDefinition, "new_name", "new_description", "new_sync");

        //asset
        taskDefinition = entityManager.find(TaskDefinition.class, 1);

        assertNotNull(taskDefinition);
        assertEquals("new_name", taskDefinition.getName());
        assertEquals("new_description", taskDefinition.getDescription());
        assertEquals("new_sync", taskDefinition.getSyncUuid());
    }

    @Test
    public void patch_TaskDefinitionMirrorChanges_applied() {
        //arrange
        TaskDefinitionMirror taskDefinitionMirror = new TaskDefinitionMirror(1, "name", null, null);

        //act
        taskDefinitionService.patch(taskDefinitionMirror, "new_name", "new_description", "new_sync");

        //asset
        taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 1);

        assertNotNull(taskDefinitionMirror);
        assertEquals("new_name", taskDefinitionMirror.getName());
        assertEquals("new_description", taskDefinitionMirror.getDescription());
        assertEquals("new_sync", taskDefinitionMirror.getSyncUuid());
    }

}