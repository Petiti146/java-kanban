package ru.yandex.practicum.tasktracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void testTaskInstances() {
        Task task1 = new Task(1, "Name 1", "Description 1");
        Task task2 = new Task(1, "Name 2", "Description 2");

        assertEquals(task1, task2);
    }

    @Test
    void testTaskChildren() {
        Subtask subtask = new Subtask("Name 1", "Description 1", 1);
        subtask.setId(1);

        Epic epic = new Epic(new Task("Name 2", "Description 2"));
        epic.setId(1);

        assertEquals(subtask, epic);
        assertEquals(epic, subtask);
    }
}
