package ru.yandex.practicum.tasktracker.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.service.HistoryManager;
import ru.yandex.practicum.tasktracker.service.InMemoryHistoryManager;
import ru.yandex.practicum.tasktracker.service.InMemoryTaskManager;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManagersTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void testGetDefaultTaskManager() {
        assertNotNull(taskManager);
    }

    @Test
    void testGetDefaultHistoryManager() {
        assertNotNull(historyManager);
    }

    @Test
    void testGetDefaultTaskManagerInstance() {
        assertTrue(taskManager instanceof InMemoryTaskManager);
    }

    @Test
    void testGetDefaultHistoryManagerInstance() {
        assertTrue(historyManager instanceof InMemoryHistoryManager);
    }
}
