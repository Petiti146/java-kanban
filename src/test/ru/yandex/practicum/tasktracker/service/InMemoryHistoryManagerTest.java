package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.util.Managers;

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
        Task task1 = new Task("Task 1", "Description");
        task1.setId(1);
        historyManager.add(task1);
        List<Task> tasks = historyManager.getHistory();

        assertEquals(task1, tasks.get(0));

        List<Task> preparedTasks = getPreparedTask();
        for (Task preparedTask : preparedTasks) {
            historyManager.add(preparedTask);
        }

        assertEquals(10, historyManager.getHistory().size());

        Task task11 = new Task("Task 11", "Description");
        task11.setId(11);
        historyManager.add(task11);

        assertEquals(10, historyManager.getHistory().size());
        assertEquals(11, historyManager.getHistory().get(9).getId());
        assertEquals(1, historyManager.getHistory().get(0).getId());
    }

    @Test
    void getHistoryTest() {
        assertEquals(0, historyManager.getHistory().size());
    }

    private List<Task> getPreparedTask() {
        Task task2 = new Task("Task 2", "Description");
        Task task3 = new Task("Task 3", "Description");
        Task task4 = new Task("Task 4", "Description");
        Task task5 = new Task("Task 5", "Description");
        Task task6 = new Task("Task 6", "Description");
        Task task7 = new Task("Task 7", "Description");
        Task task8 = new Task("Task 8", "Description");
        Task task9 = new Task("Task 9", "Description");
        Task task10 = new Task("Task 10", "Description");

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
