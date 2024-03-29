package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.util.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setup() {
        this.taskManager = Managers.getDefault();
    }

    @Test
    void testEpicCannotAddIntoEpic() {
        Task task1 = new Task("name 1", "description 1");

        Task savedTask1 = taskManager.addTask(task1);
        Subtask subtask1 = new Subtask("name 1", "description 1", savedTask1.getId());
        taskManager.addSubtask(subtask1);
        Epic savedEpic1 = taskManager.getEpicById(savedTask1.getId());

        Subtask subTask = new Subtask("name", "description", savedEpic1.getId());
        subTask.setId(savedEpic1.getId());
        Subtask subtask = taskManager.addSubtask(subTask);

        assertNull(taskManager.getSubtaskById(savedTask1.getId()));
        assertNotEquals(savedEpic1.getSubTaskIds(), subtask.getEpicId());
    }

    @Test
    void testSubtaskCannotBeItsEpic() {
        Task task1 = new Task("name 1", "description 1");

        Task savedTask1 = taskManager.addTask(task1);
        Subtask subtask1 = new Subtask("name 1", "description 1", savedTask1.getId());
        Subtask savedSubtask = taskManager.addSubtask(subtask1);
        savedSubtask.setEpicId(savedSubtask.getId());
        Subtask updatedSubtask = taskManager.updateSubtask(savedSubtask);

        assertNull(taskManager.getSubtaskById(savedTask1.getId()));
        assertNull(updatedSubtask);
    }

    @Test
    void testAddDifferentClassesOfTaskAndWeCanGetThemById() {
        Task task = new Task("Task name", "Description");
        Subtask subtask = new Subtask("Subtask name", "Description", 1);
        Epic epic = new Epic(new Task("Epic name", "Description"));

        Task savedTask = taskManager.addTask(task);
        Subtask savedSubtask = taskManager.addSubtask(subtask);
        Epic savedEpic = taskManager.addEpic(epic);

        Task savedTaskById = taskManager.getTaskById(savedTask.getId());
        Subtask savedSubtaskById = taskManager.getSubtaskById(savedSubtask.getId());
        Epic savedEpicById = taskManager.getEpicById(savedEpic.getId());

        assertNotNull(savedTask);
        assertNotNull(savedSubtask);
        assertNotNull(savedEpic);
        assertNull(savedTaskById);
        assertNotNull(savedSubtaskById);
        assertNotNull(savedEpicById);
    }

    @Test
    void testTaskWithSetIdAndGeneratedIdHaveNoConflicts() {
        Task task1 = new Task(1, "Task name", "Description");
        Task task2 = new Task("Task name", "Description");

        Task savedTask1 = taskManager.addTask(task1);
        Task savedTask2 = taskManager.addTask(task2);

        assertNotNull(savedTask1);
        assertNotNull(savedTask2);
        assertNotEquals(savedTask1.getId(), savedTask2.getId());
    }

    @Test
    void testTaskFieldsConsistencyWhileAdd() {
        Task task1 = new Task("Task name", "Description");

        Task savedTask1 = taskManager.addTask(task1);

        assertNotNull(savedTask1);
        assertEquals(task1.getName(), savedTask1.getName());
        assertEquals(task1.getDescription(), savedTask1.getDescription());
    }

    @Test
    void testUpdateTaskFields() {
        Task task1 = new Task("Task name", "Description");
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
    void testUpdateSubtaskFields() {
        Task task1 = new Task("Task name", "Task description");
        Subtask subtask1 = new Subtask("Subtask name", "Subtask description", null);
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
    void testDeleteTaskById() {
        Task task = new Task("Task name", "Task description");

        Task savedTask = taskManager.addTask(task);
        boolean isTaskDeleted = taskManager.deleteTaskById(savedTask.getId());
        Task taskById = taskManager.getTaskById(savedTask.getId());

        assertNotNull(savedTask);
        assertTrue(isTaskDeleted);
        assertNull(taskById);
    }

    @Test
    void testDeleteSubtaskById() {
        Task task = new Task("Task name", "Task description");
        Task savedTask = taskManager.addTask(task);

        Subtask subtask = new Subtask("Subtask name", "Subtask description", savedTask.getId());

        Subtask savedSubtask = taskManager.addSubtask(subtask);
        boolean isSubtaskDeleted = taskManager.deleteSubtaskById(savedSubtask.getId());
        Subtask subtaskById = taskManager.getSubtaskById(savedSubtask.getId());

        assertNotNull(savedTask);
        assertNotNull(savedSubtask);
        assertTrue(isSubtaskDeleted);
        assertNull(subtaskById);
    }

    @Test
    void testDeleteEpicById() {
        Epic epic = new Epic("Epic name", "Epic description");
        Epic savedEpic = taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", epic.getId());
        Subtask savedSubtask = taskManager.addSubtask(subtask);

        boolean isEpicDeleted = taskManager.deleteEpicById(epic.getId());

        Epic epicById = taskManager.getEpicById(epic.getId());
        Subtask subtaskById = taskManager.getSubtaskById(savedSubtask.getId());

        assertNotNull(savedEpic);
        assertNotNull(savedSubtask);
        assertTrue(isEpicDeleted);
        assertNull(epicById);
        assertNull(subtaskById);
    }
}
