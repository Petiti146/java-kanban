package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static java.util.Comparator.comparing;

public class InMemoryTaskManager implements TaskManager {

    protected int counterId;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Override
    public Task addTask(Task task) {
        task.setId(++counterId);
        if (getPrioritizedTasks().stream().anyMatch(task1 -> task1.isCrossing(task))) {
            throw new IllegalArgumentException("Задача пересекается с уже существующей задачей.");
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask addSubtask(Subtask subTask) {
        Task task = tasks.get(subTask.getEpicId());
        Epic epic = epics.get(subTask.getEpicId());
        if (task == null && epic == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        if (epic == null) {
            epic = new Epic(task, subTask);
        }
        subTask.setId(++counterId);
        tasks.remove(subTask.getEpicId());
        if (getPrioritizedTasks().stream().anyMatch(task1 -> task1.isCrossing(subTask))) {
            throw new IllegalArgumentException("Задача пересекается с уже существующей задачей.");
        }
        epic.addSubTaskId(subTask.getId());
        epic.setDuration(epic.getDuration().plus(subTask.getDuration()));
        if (subTask.getEndTime().isAfter(epic.getEndTime())) {
            epic.setEndTime(subTask.getEndTime());
        }
        if (subTask.getStartTime().isBefore(epic.getStartTime())) {
            epic.setStartTime(subTask.getStartTime());
        }
        epics.put(epic.getId(), epic);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> sortedAllTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        sortedAllTasks.addAll(tasks.values().stream().filter(task -> task.getStartTime() != null).toList());
        sortedAllTasks.addAll(subTasks.values().stream().filter(task -> task.getStartTime() != null).toList());
        return sortedAllTasks;
    }

    @Override
    public Task updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
        savedTask.setStatus(task.getStatus());
        if (getPrioritizedTasks().stream().filter(t -> t.getId() != task.getId()).anyMatch(task1 -> task1.isCrossing(task))) {
            throw new IllegalArgumentException("Задача пересекается с уже существующей задачей.");
        }
        savedTask.setDuration(task.getDuration());
        savedTask.setStartTime(task.getStartTime());
        return savedTask;
    }

    @Override
    public Subtask updateSubtask(Subtask subTask) {
        Subtask savedSubTask = subTasks.get(subTask.getId());
        if (savedSubTask == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        savedSubTask.setName(subTask.getName());
        savedSubTask.setDescription(subTask.getDescription());
        savedSubTask.setStatus(subTask.getStatus());
        if (getPrioritizedTasks().stream().filter(t -> t.getId() != subTask.getId()).anyMatch(task1 -> task1.isCrossing(subTask))) {
            throw new IllegalArgumentException("Задача пересекается с уже существующей задачей.");
        }

        savedSubTask.setStartTime(subTask.getStartTime());
        if (!epics.containsKey(savedSubTask.getEpicId())) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        Epic epic = epics.get(savedSubTask.getEpicId());
        if (epic == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        updateEpicStatus(savedSubTask.getEpicId());
        List<Subtask> subtasks = getEpicSubtasks(subTask.getEpicId());
        epic.setEndTime(subtasks.stream().max(comparing(Task::getEndTime)).get().getEndTime());
        epic.setStartTime(subtasks.stream().min(comparing(Task::getStartTime)).get().getStartTime());
        epic.setDuration(epic.getDuration().minus(savedSubTask.getDuration()).plus(subTask.getDuration()));
        savedSubTask.setDuration(subTask.getDuration());
        return savedSubTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        if (epic.getStatus() == TaskStatus.IN_PROGRESS) {
            savedEpic.setStatus(epic.getStatus());
        }
        return savedEpic;
    }

    @Override
    public boolean deleteTaskById(int id) {
        historyManager.remove(id);
        return tasks.remove(id) != null;
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        Subtask subTask = subTasks.get(id);
        if (subTask == null) {
            return false;
        }
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            throw new NullPointerException("Эпика не существует");
        }
        if (!epic.removeSubTaskId(id)) {
            throw new NullPointerException("Сабтаск не найден");
        }
        List<Subtask> subtasks = getEpicSubtasks(subTask.getEpicId());
        if (!subtasks.isEmpty()) {
            epic.setEndTime(subtasks.stream().max(comparing(Task::getEndTime)).get().getEndTime());
            epic.setStartTime(subtasks.stream().min(comparing(Task::getStartTime)).get().getStartTime());
        } else {
            epic.setEndTime(null);
            epic.setStartTime(null);
        }
        epic.setDuration(epic.getDuration().minus(subTask.getDuration()));
        updateEpicStatus(epic.getId());
        historyManager.remove(id);
        return subTasks.remove(id) != null;
    }

    @Override
    public boolean deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return false;
        }
        for (Integer subTaskId : epic.getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        historyManager.remove(id);
        return epics.remove(id) != null;
    }

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        for (Integer subTaskId : epic.getSubTaskIds()) {
            Subtask subTask = subTasks.get(subTaskId);
            if (subTask == null) {
                epic.removeAllSubTaskIds();
                epic.setStatus(TaskStatus.NEW);
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

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subTasks.get(id);
        if (subtask == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NullPointerException("Указан не верный айди задачи");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer i : tasks.keySet()) {
            historyManager.remove(i);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Subtask subtask : new ArrayList<>(subTasks.values())) {
            Epic savedEpic = epics.get(subtask.getEpicId());
            subTasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
            updateEpicStatus(savedEpic.getId());
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Integer i : epics.keySet()) {
            historyManager.remove(i);
        }
        for (Integer i : subTasks.keySet()) {
            historyManager.remove(i);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            throw new NullPointerException("Эпик с таким айди не существует");
        }
        List<Subtask> subtasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTaskIds()) {
            Subtask subTask = subTasks.get(subTaskId);
            subtasks.add(subTask);
        }
        return subtasks;
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
