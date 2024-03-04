package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;//спасибо большое за это замечание, исправил импорты, теперь коректные

public class InMemoryTaskManager implements TaskManager {

    private int idCounter;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Override
    public Task addTask(Task task) {
        task.setId(++idCounter);
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    @Override
    public Subtask addSubtask(Subtask subTask) {
         Task savedTaskOrEpic = subTasks.get(subTask.getEpicId());//поменял

        if (savedTaskOrEpic == null) {
            return null;
        }
        subTask.setId(++idCounter);
        tasks.remove(subTask.getEpicId());
        Epic epic = new Epic(savedTaskOrEpic);
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
        epic.setId(++idCounter);
        epics.put(epic.getId(), epic);
        return epics.get(epic.getId());
    }

    @Override
    public Task updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());//я ревью замечание отметил как выполненое
        if (savedTask == null) {//щас не могу посмотреть как ты просил назвать её
            return null;//savedTask подходит, таск1 да, глупо выглядит
        }
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
        savedTask.setStatus(task.getStatus());
        return savedTask;
    }

    @Override
    public Subtask updateSubtask(Subtask subTask) {
        Subtask subtask1 = subTasks.get(subTask.getId());
        if (subtask1 == null) {
            return null;
        }
        subtask1.setName(subTask.getName());
        subtask1.setDescription(subTask.getDescription());
        subtask1.setStatus(subTask.getStatus());

        if (!epics.containsKey(subtask1.getEpicId())) {
            return null;
        }

        Epic updatedEpic = updateEpic(updateEpic(epics.get(subtask1.getEpicId())));

        if (updatedEpic == null) {
            return null;
        }
        updateEpicStatus(subtask1.getEpicId());
        return subtask1;
    }

    private Epic updateEpic(Epic epic) {
        Epic epic1 = epics.get(epic.getId());
        if (epic1 == null) {
            return null;
        }
        epic1.setName(epic.getName());
        epic1.setDescription(epic.getDescription());
        if (epic.getStatus() == TaskStatus.IN_PROGRESS) {
            epic1.setStatus(epic.getStatus());
        }
        return epic1;
    }

    @Override
    public boolean deleteTaskById(int id) {
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
        subTasks.remove(id);
        updateEpicStatus(epic.getId());
        checkEpicAndConvertToTask();
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
        epics.remove(id);
        return true;
    }

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return;
        }
        if (epic.getStatus().equals(TaskStatus.DONE)) {
            return;
        }
        for (Integer subTaskId : epic.getSubTaskIds()) {
            Subtask subTask = subTasks.get(subTaskId);
            if (subTask == null) {
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
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subTasks.get(id) != null) {
            historyManager.add(subTasks.get(id));
        }
        return this.subTasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
        }
        return this.epics.get(id);
    }

    @Override
    public void deleteAllTasks() {
        this.tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        this.subTasks.clear();
        checkEpicAndConvertToTask();
    }

    @Override
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

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getSubtasks() {
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
    public List<Task> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
