import model.Epic;
import model.Status;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();
    private int epicIdCounter;
    private int taskIdCounter;

    public Epic createEpic(Epic epic) {
        epic.setId(++epicIdCounter);
        epics.put(epic.getId(), epic);
        return epics.get(epic.getId());
    }

    public List<Epic> readEpicList() {
        return (List<Epic>) epics.values();
    }

    public Epic readEpicById(int id) {
        if (epics.get(id) == null) {
            System.out.println("Эпика под таким id не существует");
            return null;
        }
        return epics.get(id);
    }

    public void deleteEpicById(int id) {
        if (epics.get(id) == null) {
            System.out.println("Эпика под таким id не существует");
            return;
        }
        epics.remove(id);
    }

    public void updateEpicById(int id, Epic epic) {
        if (epics.get(id) == null) {
            System.out.println("Эпика под таким id не существует");
            return;
        }
        Epic oldEpic = epics.get(id);// из базы данных достаю эпик по id
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
        oldEpic.setStatus(epic.getStatus());
        epics.put(oldEpic.getId(), oldEpic);
    }

    public Task createTask(Task task) {
        if (task.getEpicId() != null) {
            if (epics.get(task.getEpicId()) == null) {
                System.out.println("Такого эпика не существует");
                return null;
            }
            task.setId(++taskIdCounter);
            tasks.put(task.getId(), task);
            return tasks.get(task.getId());
        }

        task.setId(++taskIdCounter);
        tasks.put(task.getId(), task);

        Epic epic = readEpicById(task.getEpicId());
        epic.setStatus(Status.IN_PROGRESS);
        updateEpicById(epic.getId(), epic);

        return tasks.get(task.getId());
    }

    public List<Task> readTaskList() {
        return (List<Task>) tasks.values();
    }

    public Task readTaskById(int id) {
        if (tasks.get(id) == null) {
            System.out.println("Задачи под таким id не существует");
            return null;
        }
        return tasks.get(id);
    }

    public void deleteTaskById(int id) {
        Task task = readTaskById(id);
        if (tasks.get(id) == null) {
            System.out.println("Задачи под таким id не существует");
            return;
        }
        tasks.remove(id);

        updateEpicStatus(task);
    }


    public void updateTaskById(int id, Task task) {
        Task oldTask = tasks.get(id);
        oldTask.setName(task.getName());
        oldTask.setDescription(task.getDescription());
        oldTask.setStatus(task.getStatus());
        oldTask.setEpicId(task.getEpicId());
        tasks.put(oldTask.getId(), oldTask);

        updateEpicStatus(oldTask);
    }

    public void displayEpics() {
        for (Epic epic : epics.values()) {
            System.out.printf("%d. %s (%s)%n", epic.getId(), epic.getName(), epic.getStatus());
        }
    }

    public void displayTasks() {
        for (Task task : tasks.values()) {
            System.out.printf("%d. %s (%s)%n", task.getId(), task.getName(), task.getStatus());
        }
    }

    private void updateEpicStatus(Task task) {
        Epic epic = readEpicById(task.getId());
        List<Task> epicTasks = new ArrayList<>();
        for (Task t : tasks.values()) {
            if (t.getEpicId() != null && epic.getId() == t.getEpicId()) {
                epicTasks.add(t);
            }
        }

        List<Task> epicTasksWereDone = new ArrayList<>();
        for (Task t : epicTasks) {
            if (Status.DONE.equals(t.getStatus())) {
                epicTasksWereDone.add(t);
            }
        }

        if (epicTasks.size() == epicTasksWereDone.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        updateEpicById(epic.getId(), epic);
    }
}
