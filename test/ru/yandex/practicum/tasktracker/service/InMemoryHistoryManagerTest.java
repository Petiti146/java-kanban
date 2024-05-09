package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setup() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addNewTask() {
        Task task1 = new Task("Task 1", "Description", Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        task1.setId(1);
        historyManager.add(task1);
        List<Task> tasks = historyManager.getHistory();

        assertEquals(task1, tasks.get(0));
    }

    @Test
    void getHistoryTest() {
        assertEquals(0, historyManager.getHistory().size());
    }

    private List<Task> getPreparedTask() {
        Task task2 = new Task("Task 2", "Description", Duration.ofMinutes(33), LocalDateTime.of(2001, 12, 18, 3, 32));
        Task task3 = new Task("Task 3", "Description", Duration.ofMinutes(33), LocalDateTime.of(2002, 12, 18, 3, 32));
        Task task4 = new Task("Task 4", "Description", Duration.ofMinutes(33), LocalDateTime.of(2003, 12, 18, 3, 32));
        Task task5 = new Task("Task 5", "Description", Duration.ofMinutes(33), LocalDateTime.of(2004, 12, 18, 3, 32));
        Task task6 = new Task("Task 6", "Description", Duration.ofMinutes(33), LocalDateTime.of(2005, 12, 18, 3, 32));
        Task task7 = new Task("Task 7", "Description", Duration.ofMinutes(33), LocalDateTime.of(2006, 12, 18, 3, 32));
        Task task8 = new Task("Task 8", "Description", Duration.ofMinutes(33), LocalDateTime.of(2007, 12, 18, 3, 32));
        Task task9 = new Task("Task 9", "Description", Duration.ofMinutes(33), LocalDateTime.of(2008, 12, 18, 3, 32));
        Task task10 = new Task("Task 10", "Description", Duration.ofMinutes(33), LocalDateTime.of(2009, 12, 18, 3, 32));

        List<Task> preparedTasks = new ArrayList<>();
        preparedTasks.add(task2);
        preparedTasks.add(task3);
        preparedTasks.add(task4);
        preparedTasks.add(task5);
        preparedTasks.add(task6);
        preparedTasks.add(task7);
        preparedTasks.add(task8);
        preparedTasks.add(task9);
        preparedTasks.add(task10);

        for (int i = 0; i < preparedTasks.size(); i++) {
            preparedTasks.get(i).setId(i + 1);
        }
        return preparedTasks;
    }
}
