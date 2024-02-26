package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.service.model.Epic;
import ru.yandex.practicum.tasktracker.service.model.SubTask;
import ru.yandex.practicum.tasktracker.service.model.Task;
import ru.yandex.practicum.tasktracker.service.model.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    public boolean addTask(Task task) {
        return tasks.put(task.getId(), task) != null;
    }

    public boolean addSubTask(SubTask subTask) {
        if (subTasks.get(subTask.getId()) != null) {
            System.out.print("Уже есть такая subtask");
            return false;
        }
        Task task = tasks.get(subTask.getParentId());
        if (task == null) {
            System.out.print("Задачи с такой id нет");
            return false;
        }
        tasks.remove(subTask.getParentId());
        Epic epic = new Epic(task);
        epic.addSubTaskId(subTask.getId());
        epics.put(epic.getId(), epic);
        subTasks.put(subTask.getId(), subTask);
        return true;
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
        updateEpic(updateEpic(epics.get(subTask1.getParentId())));
        updateStatus(subTask1.getParentId());
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
        if (epic.getStatus() == Status.IN_PROGRESS) {
            epic1.setStatus(epic.getStatus());
        }
        return epic1;
    }

    public boolean deleteTaskById(int id) {
        return tasks.remove(id) != null;
    }

    public boolean deleteSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            System.out.println("Такого сабтаска не существует");
            return false;
        }
        Epic epic = epics.get(subTask.getParentId());
        if (epic == null) {
            System.out.println("Такого эпика не существует");
            return false;
        }
        epic.removeSubTaskId(id);
        updateStatus(epic.getId());
        subTasks.remove(id);
        return true;
    }

    public boolean deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Такого эпика не существует");
            return false;
        }
        for (Integer subTaskId : epic.getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
        return true;
    }

    public boolean updateStatus(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Такого эпика не существует");
            return false;
        }
        if (epic.getStatus().equals(Status.DONE)) {
            return false;
        }
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask == null) {
                System.out.println("Такого сабтаска не существует");
                return false;
            }
            if (subTask.getStatus().equals(Status.NEW)) {
                return false;
            }
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return true;
            }
        }
        epic.setStatus(Status.DONE);
        return true;
    }

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("Задачи под таким id не существует");
            return null;
        }
        return task;
    }

    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            System.out.println("Подзадачи под таким id не существует");
            return null;
        }
        return subTask;
    }

    public ArrayList<SubTask> getSubtasksByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпика под таким id не существует");
            return null;
        }
        ArrayList<SubTask> tasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask == null) {
                System.out.println("Такого сабтаска не существует");
                return null;
            }
            tasks.add(subTask);
        }
        return tasks;
    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпика под таким id не существует");
            return null;
        }
        return epic;
    }

    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteAllSubTaskAndEpic() {
        subTasks.clear();
        epics.clear();
    }

    public void displayTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void displaySubTasks() {
        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    public void displayEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }
}
