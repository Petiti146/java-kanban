package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.util.List;

public interface TaskManager {
    Task addTask(Task task);

    Subtask addSubtask(Subtask subTask);

    Epic addEpic(Epic epic);

    Task updateTask(Task task);

    Subtask updateSubtask(Subtask subTask);

    boolean deleteTaskById(int id);

    boolean deleteSubtaskById(int id);

    boolean deleteEpicById(int id);

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpics();

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Subtask> getEpicSubtasks(int epicId);

    List<Epic> getEpics();

    List<Task> getHistory();
}
