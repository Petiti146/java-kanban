package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int counterId;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Override
    public Task addTask(Task task) {
        task.setId(++counterId);
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    @Override
    public Subtask addSubtask(Subtask subTask) {
        Epic epic = null;
        Task savedTaskOrEpic = tasks.get(subTask.getEpicId());
        Task savedTaskOrEpicFromEpics = epics.get(subTask.getEpicId());

        if (savedTaskOrEpic == null && savedTaskOrEpicFromEpics == null) {
            return null;
        }
        if (savedTaskOrEpicFromEpics != null) {
            epic = new Epic(savedTaskOrEpicFromEpics);
        } else {
            epic = new Epic(savedTaskOrEpic);
        }
        subTask.setId(++counterId);
        tasks.remove(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
        epics.put(epic.getId(), epic);
        subTasks.put(subTask.getId(), subTask);
        return subTasks.get(subTask.getId());
    }

    private Task getTaskOrEpicAsTask(Integer epicId) {
        if (tasks.containsKey(epicId)) {
            return tasks.get(epicId);
        } else if (epics.containsKey(epicId)) {
            return epics.get(epicId);
        }
        return null;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(++counterId);
        epics.put(epic.getId(), epic);
        return epics.get(epic.getId());
    }

    @Override
    public Task updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return null;
        }
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
        savedTask.setStatus(task.getStatus());
        return savedTask;
    }

    @Override
    public Subtask updateSubtask(Subtask subTask) {
        Subtask savedSubTask = subTasks.get(subTask.getId());
        if (savedSubTask == null) {
            return null;
        }
        savedSubTask.setName(subTask.getName());
        savedSubTask.setDescription(subTask.getDescription());
        savedSubTask.setStatus(subTask.getStatus());

        if (!epics.containsKey(savedSubTask.getEpicId())) {
            return null;
        }

        Epic updatedEpic = updateEpic(updateEpic(epics.get(savedSubTask.getEpicId())));

        if (updatedEpic == null) {
            return null;
        }
        updateEpicStatus(savedSubTask.getEpicId());
        return savedSubTask;
    }

    private Epic updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return null;
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
            return false;
        }
        epic.removeSubTaskId(id);
        historyManager.remove(id);
        subTasks.remove(id);
        updateEpicStatus(epic.getId());
        return true;
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
        epics.remove(id);
        return true;
    }

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return;
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
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return this.epics.get(id);
    }

    @Override
    public void deleteAllTasks() {
        for (Integer i : this.tasks.keySet()) {
            historyManager.remove(i);
        }
        this.tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        final Map<Integer, Subtask> savedAllSubTasks = new HashMap<>(subTasks);
        for (Subtask subtask : savedAllSubTasks.values()) {
            Epic savedEpic = epics.get(subtask.getEpicId());
            subTasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
            updateEpicStatus(savedEpic.getId());
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Integer i : this.epics.keySet()) {
            historyManager.remove(i);
        }
        for (Integer i : this.subTasks.keySet()) {
            historyManager.remove(i);
        }
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
            return new ArrayList<>();
        }
        List<Subtask> subtasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTaskIds()) {
            Subtask subTask = subtasks.get(subTaskId);
            if (subTask == null) {
                return new ArrayList<>();
            }
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

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }
}
