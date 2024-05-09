package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setup() {
        this.taskManager = Managers.getDefault();
    }

    @Test
    void testEpicCannotAddIntoEpic() throws Exception {
        Task task1 = new Task("name 1", "description 1",
                Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));

        Task savedTask1 = taskManager.addTask(task1);
        Subtask subtask1 = new Subtask("name 1", "description 1", savedTask1.getId(),
                Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        taskManager.addSubtask(subtask1);
        Epic savedEpic1 = taskManager.getEpicById(savedTask1.getId());

        Subtask subTask = new Subtask("name", "description",
                savedEpic1.getId(), Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        subTask.setId(savedEpic1.getId());
        Subtask subtask = subTask;

        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(savedTask1.getId()));
        assertNotEquals(savedEpic1.getSubTaskIds(), subtask.getEpicId());
    }

    @Test
    void testSubtaskCannotBeItsEpic() throws Exception {
        Task task1 = new Task("name 1", "description 1", Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));

        Task savedTask1 = taskManager.addTask(task1);
        Subtask subtask1 = new Subtask("name 1", "description 1", savedTask1.getId(), Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        Subtask savedSubtask = taskManager.addSubtask(subtask1);
        savedSubtask.setEpicId(savedSubtask.getId());

        assertThrows(NullPointerException.class, () -> taskManager.updateSubtask(savedSubtask));
        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(savedTask1.getId()));
    }

    @Test
    void testAddDifferentClassesOfTaskAndWeCanGetThemById() throws Exception {
        Task task = new Task("Task name", "Description", Duration.ofMinutes(33), LocalDateTime.of(2001, 12, 18, 3, 32));
        Subtask subtask = new Subtask("Subtask name", "Description", 1, Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        Epic savedEpic = new Epic(new Task("Epic name", "Description", Duration.ofMinutes(33), LocalDateTime.of(2003, 12, 18, 3, 32))
        , new Subtask("Epic name", "Description", 1, Duration.ofMinutes(33), LocalDateTime.of(2004, 12, 18, 3, 32)));
        Subtask subtask1 = new Subtask("Subtask name", "Description", 12, Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));

        Task savedTask = taskManager.addTask(task);
        Subtask savedSubtask = taskManager.addSubtask(subtask);
        savedEpic.setId(12);
        assertThrows(NullPointerException.class, () -> taskManager.addSubtask(subtask1));
        Subtask savedSubtaskById = taskManager.getSubtaskById(savedSubtask.getId());
        assertThrows(NullPointerException.class, () -> taskManager.getEpicById(savedEpic.getId()));

        assertNotNull(savedTask);
        assertNotNull(savedSubtask);
        assertNotNull(savedEpic);
        assertNotNull(savedSubtaskById);
    }

    @Test
    void testTaskWithSetIdAndGeneratedIdHaveNoConflicts() throws Exception {
        Task task1 = new Task("Task name", "Description", Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));
        task1.setId(1);
        Task task2 = new Task("Task name", "Description", Duration.ofMinutes(33), LocalDateTime.of(2004, 12, 18, 3, 32));

        Task savedTask1 = taskManager.addTask(task1);
        Task savedTask2 = taskManager.addTask(task2);

        assertNotNull(savedTask1);
        assertNotNull(savedTask2);
        assertNotEquals(savedTask1.getId(), savedTask2.getId());
    }

    @Test
    void testTaskFieldsConsistencyWhileAdd() throws Exception {
        Task task1 = new Task("Task name", "Description", Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));

        Task savedTask1 = taskManager.addTask(task1);

        assertNotNull(savedTask1);
        assertEquals(task1.getName(), savedTask1.getName());
        assertEquals(task1.getDescription(), savedTask1.getDescription());
    }

    @Test
    void testUpdateTaskFields() throws Exception {
        Task task1 = new Task("Task name", "Description", Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));
        String updatedTaskName = "Updated task name";
        String updatedTaskDescription = "Updated task description";

        Task savedTask1 = taskManager.addTask(task1);
        savedTask1.setName(updatedTaskName);
        savedTask1.setDescription(updatedTaskDescription);

        Task updatedTask1 = taskManager.updateTask(savedTask1);

        assertNotNull(updatedTask1);
        assertEquals(updatedTaskName, updatedTask1.getName());
        assertEquals(updatedTaskDescription, updatedTask1.getDescription());
    }

    @Test
    void testUpdateSubtaskFields() throws Exception {
        Task task1 = new Task("Task name", "Task description", Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));
        Subtask subtask1 = new Subtask("Subtask name", "Subtask description", 1,Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));
        String updatedSubtaskName = "Updated task name";
        String updatedSubtaskDescription = "Updated task description";

        Task savedTask1 = taskManager.addTask(task1);
        subtask1.setEpicId(savedTask1.getId());

        Subtask savedSubtask1 = taskManager.addSubtask(subtask1);
        savedSubtask1.setName(updatedSubtaskName);
        savedSubtask1.setDescription(updatedSubtaskDescription);

        Subtask updatedSubtask1 = taskManager.updateSubtask(savedSubtask1);

        assertNotNull(updatedSubtask1);
        assertEquals(updatedSubtaskName, updatedSubtask1.getName());
        assertEquals(updatedSubtaskDescription, updatedSubtask1.getDescription());
    }

    @Test
    void testDeleteTaskById() throws Exception {
        Task task = new Task("Task name", "Task description", Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));
        taskManager.addTask(task);
        Task savedTask = task;
        boolean isTaskDeleted = taskManager.deleteTaskById(savedTask.getId());

        assertThrows(NullPointerException.class, () -> taskManager.getTaskById(savedTask.getId()));
        assertNotNull(savedTask);
        assertTrue(isTaskDeleted);
    }

    @Test
    void testDeleteSubtaskById() throws Exception {
        Task savedTask = new Task("Task name", "Task description", Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));
        taskManager.addTask(savedTask);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", savedTask.getId(), Duration.ofMinutes(33), LocalDateTime.of(2003, 12, 18, 23, 32));
        taskManager.addSubtask(subtask);
        boolean isSubtaskDeleted = taskManager.deleteSubtaskById(subtask.getId());

        assertNotNull(savedTask);
        assertNotNull(subtask);
        assertTrue(isSubtaskDeleted);
    }

    @Test
    void testDeleteEpicById() throws Exception {
        Task task = new Task("Epic name", "Description", Duration.ofMinutes(33), LocalDateTime.of(2003, 12, 18, 3, 32));
        taskManager.addTask(task);
        Subtask subtask = new Subtask("Epic name", "Description", task.getId(), Duration.ofMinutes(33), LocalDateTime.of(2003, 12, 18, 3, 32));
        taskManager.addSubtask(subtask);
        taskManager.deleteEpicById(task.getId());

        assertThrows(NullPointerException.class, () -> taskManager.getEpicById(task.getId()));
    }
}
