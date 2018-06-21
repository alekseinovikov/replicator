package net.task.replicator.services.impl;

import net.task.replicator.entities.TaskDefinition;
import net.task.replicator.entities.TaskDefinitionMirror;
import net.task.replicator.repositories.TaskDefinitionMirrorRepository;
import net.task.replicator.repositories.TaskDefinitionRepository;
import net.task.replicator.services.ReplicationProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class DefaultReplicationProcessorTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TaskDefinitionRepository taskDefinitionRepository;

    @Autowired
    private TaskDefinitionMirrorRepository taskDefinitionMirrorRepository;

    private ReplicationProcessor replicationProcessor;


    @Before
    public void init() {
        replicationProcessor = new DefaultReplicationProcessor(new DefaultTaskDefinitionService(taskDefinitionRepository, taskDefinitionMirrorRepository));
    }


    @Test
    public void startReplication() {
        //arrange
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(1, 'name1', 'description2')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(2, 'name2', null)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description, sync_uuid) VALUES(3, 'name3', 'description2', 'test_uuid')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition(id, name, description, sync_uuid) VALUES(4, 'name4', 'description4', 'updated')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description, sync_uuid) VALUES(4, 'name_updated', 'description_updated', 'updated')").executeUpdate();

        //act
        replicationProcessor.startReplication();

        //assert
        //Check creation
        TaskDefinitionMirror taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 1);
        assertNotNull(taskDefinitionMirror);
        assertEquals("name1", taskDefinitionMirror.getName());
        assertEquals("description2", taskDefinitionMirror.getDescription());

        //Check creation
        taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 2);
        assertNotNull(taskDefinitionMirror);
        assertEquals("name2", taskDefinitionMirror.getName());
        assertNull(taskDefinitionMirror.getDescription());

        //Check delete
        TaskDefinition taskDefinition = entityManager.find(TaskDefinition.class, 3);
        assertNull(taskDefinition);

        //Check update
        taskDefinition = entityManager.find(TaskDefinition.class, 4);
        assertNotNull(taskDefinition);
        assertEquals("name_updated", taskDefinition.getName());
        assertEquals("description_updated", taskDefinition.getDescription());
    }

}