package net.task.replicator;

import net.task.replicator.entities.TaskDefinition;
import net.task.replicator.entities.TaskDefinitionMirror;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReplicatorApplicationTests {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private EntityManager entityManager;

    private EntityManager nativeEntityManager;

    private final static Long WAIT_TIME_MLS = 1000L;

    @Test
    public void fullTestCase() throws InterruptedException {
        //Check creation
        startTransaction();
        nativeEntityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(1, 'name1', 'description1')").executeUpdate();
        nativeEntityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(2, 'name2', 'description2')").executeUpdate();
        nativeEntityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(3, 'name3', 'description3')").executeUpdate();
        nativeEntityManager.createNativeQuery("INSERT INTO task_definition(id, name, description) VALUES(4, 'name4', 'description4')").executeUpdate();
        commitTransaction();

        //Wait the replication
        sleep();

        TaskDefinitionMirror taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 1);
        assertNotNull(taskDefinitionMirror);
        assertEquals("name1", taskDefinitionMirror.getName());
        assertEquals("description1", taskDefinitionMirror.getDescription());

        taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 2);
        assertNotNull(taskDefinitionMirror);
        assertEquals("name2", taskDefinitionMirror.getName());
        assertEquals("description2", taskDefinitionMirror.getDescription());

        taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 3);
        assertNotNull(taskDefinitionMirror);
        assertEquals("name3", taskDefinitionMirror.getName());
        assertEquals("description3", taskDefinitionMirror.getDescription());

        taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 4);
        assertNotNull(taskDefinitionMirror);
        assertEquals("name4", taskDefinitionMirror.getName());
        assertEquals("description4", taskDefinitionMirror.getDescription());

        //Check update
        startTransaction();
        nativeEntityManager.createNativeQuery("UPDATE task_definition_mirror SET name ='updated_name', description='updated_description'").executeUpdate();
        commitTransaction();

        //Wait
        sleep();

        for (int i = 1; i < 5; i++) {
            TaskDefinition taskDefinition = entityManager.find(TaskDefinition.class, i);
            assertNotNull(taskDefinition);
            assertEquals("updated_name", taskDefinition.getName());
            assertEquals("updated_description", taskDefinition.getDescription());
        }

        //Changes from another side
        startTransaction();
        nativeEntityManager.createNativeQuery("INSERT INTO task_definition_mirror(id, name, description) VALUES(5, 'name5', 'description5')").executeUpdate();
        nativeEntityManager.createNativeQuery("DELETE FROM task_definition_mirror WHERE id = 1").executeUpdate();
        nativeEntityManager.createNativeQuery("UPDATE task_definition SET name = 'BMW_NAME' WHERE id = 2").executeUpdate();
        commitTransaction();

        //Wait
        sleep();

        //Check it
        TaskDefinition taskDefinition = entityManager.find(TaskDefinition.class, 5);
        assertNotNull(taskDefinition);
        assertEquals("name5", taskDefinition.getName());
        assertEquals("description5", taskDefinition.getDescription());

        taskDefinition = entityManager.find(TaskDefinition.class, 1);
        assertNull(taskDefinition);

        taskDefinitionMirror = entityManager.find(TaskDefinitionMirror.class, 2);
        assertNotNull(taskDefinitionMirror);
        assertEquals("BMW_NAME", taskDefinitionMirror.getName());
        assertEquals("updated_description", taskDefinitionMirror.getDescription());
    }

    private void startTransaction() {
        nativeEntityManager = entityManagerFactory.createEntityManager();
        nativeEntityManager.getTransaction().begin();
    }

    private void commitTransaction() {
        nativeEntityManager.flush();
        nativeEntityManager.getTransaction().commit();
        nativeEntityManager.close();
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(WAIT_TIME_MLS);
    }

}
