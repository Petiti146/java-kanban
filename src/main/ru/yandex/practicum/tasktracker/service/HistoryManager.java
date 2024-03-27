package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.util.Node;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void linkLast(Task task);

    void remove(int id);

    List<Task> getHistory();
}
