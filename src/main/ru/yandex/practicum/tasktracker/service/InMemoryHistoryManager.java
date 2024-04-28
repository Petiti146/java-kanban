package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> tasks = new ArrayList<>(11);

    @Override
    public void add(Task task) {//я понял о чем ты, спасибо, правда безполезно там ифы делаю
        if (task != null) {
            tasks.add(task);
        }
        if (tasks.size() > 10) {
            tasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasks;
    }
}
