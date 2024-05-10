package ru.yandex.practicum.tasktracker.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void testTaskInstances() {
        Task task1 = new Task("Name 1", "Description 1", Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        Task task2 = new Task("Name 2", "Description 2", Duration.ofMinutes(33), LocalDateTime.of(2004, 12, 18, 3, 32));

        assertEquals(task1, task2);
    }

    @Test
    void testTaskChildren() {
        Subtask subtask = new Subtask("Name 1", "Description 1", 1, Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        subtask.setId(1);

        Epic epic = new Epic(new Task("Name 1", "Description 1", Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32))
                ,new Subtask("Name 1", "Description 1", 1, Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32)));

        epic.setId(1);
        assertEquals(subtask, epic);
        assertEquals(epic, subtask);
    }
}
