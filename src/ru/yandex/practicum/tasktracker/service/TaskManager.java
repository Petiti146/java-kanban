package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.SubTask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private int idCounter;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    public void addTask(Task task) {
        task.setId(++idCounter);
        tasks.put(task.getId(), task);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(++idCounter);
        Task task = tasks.get(subTask.getEpicId());
        if (task == null) {
            System.out.print("Задачи с такой id нет");
            return;
        }
        tasks.remove(subTask.getEpicId());
        Epic epic = new Epic(task);
        epic.addSubTaskId(subTask.getId());
        epics.put(epic.getId(), epic);
        subTasks.put(subTask.getId(), subTask);
    }

    public Task updateTask(Task task) {
        Task task1 = tasks.get(task.getId());
        if (task1 == null) {
            System.out.println("Задачи с таким id не существует");
            return null;
        }
        task1.setName(task.getName());
        task1.setDescription(task.getDescription());
        task1.setStatus(task.getStatus());
        return task1;
    }

    public SubTask updateSubTask(SubTask subTask) {
        SubTask subTask1 = subTasks.get(subTask.getId());
        if (subTask1 == null) {
            System.out.println("Сабтаска с таким id не существует");
            return null;
        }
        subTask1.setName(subTask.getName());
        subTask1.setDescription(subTask.getDescription());
        subTask1.setStatus(subTask.getStatus());
        updateEpic(updateEpic(epics.get(subTask1.getEpicId())));
        updateEpicStatus(subTask1.getEpicId());
        return subTask1;
    }

    public Epic updateEpic(Epic epic) {
        Epic epic1 = epics.get(epic.getId());
        if (epic1 == null) {
            System.out.println("Эпика с таким id не существует");
            return null;
        }
        epic1.setName(epic.getName());
        epic1.setDescription(epic.getDescription());
        if (epic.getStatus() == TaskStatus.IN_PROGRESS) {
            epic1.setStatus(epic.getStatus());
        }
        return epic1;
    }

    public boolean deleteTaskById(int id) {
        return tasks.remove(id) != null;
    }

    public void deleteSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            System.out.println("Такого сабтаска не существует");
            return;
        }
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            System.out.println("Такого эпика не существует");
            return;
        }
        epic.removeSubTaskId(id);
        subTasks.remove(id);
        updateEpicStatus(epic.getId());
        checkEpicAndConvertToTask();
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Такого эпика не существует");
            return;
        }
        for (Integer subTaskId : epic.getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    public void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Такого эпика не существует");
            return;
        }
        if (epic.getStatus().equals(TaskStatus.DONE)) {
            return;
        }
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask == null) {
                System.out.println("Такого сабтаска не существует");
                return;
            }
            if (subTask.getStatus().equals(TaskStatus.NEW)) {
                return;
            }
            if (subTask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }
        epic.setStatus(TaskStatus.DONE);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return this.subTasks.get(id);
    }

    public ArrayList<SubTask> getSubtasksByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпика под таким id не существует");
            return new ArrayList<>();
        }
        ArrayList<SubTask> tasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask == null) {
                System.out.println("Такого сабтаска не существует");
                return new ArrayList<>();
            }
            tasks.add(subTask);
        }
        return tasks;
    }

    public Epic getEpicById(int id) {
        return this.epics.get(id);
    }

    public void deleteAllTasks() {
        this.tasks.clear();
    }

    public void deleteAllSubTasks() {
        this.subTasks.clear();
        checkEpicAndConvertToTask();
    }

    public void deleteAllEpics() {
        this.epics.clear();
        this.subTasks.clear();
    }

    private void checkEpicAndConvertToTask() {
        Collection<Epic> epics = new ArrayList<>(this.epics.values());
        for (Epic epic : epics) {
            checkEpicByIdAndConvertToTask(epic.getId());
        }
    }

    private void checkEpicByIdAndConvertToTask(int id) {
        Epic epic = getEpicById(id);
        if (epic.getSubTaskIds().isEmpty()) {
            Task task = new Task(epic.getName(), epic.getDescription());
            task.setId(epic.getId());
            deleteEpicById(epic.getId());
            this.tasks.put(task.getId(), task);
        }
    }
}
