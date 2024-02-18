import model.Epic;
import model.Task;

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
        Epic oldEpic = epics.get(id);
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
        oldEpic.setStatus(epic.getStatus());
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
        if (tasks.get(id) == null) {
            System.out.println("Задачи под таким id не существует");
            return;
        }
        tasks.remove(id);
    }

    public void updateTaskById(int id, Task task) {
        Task oldEpic = tasks.get(id);
        oldEpic.setName(task.getName());
        oldEpic.setDescription(task.getDescription());
        oldEpic.setStatus(task.getStatus());
        oldEpic.setEpicId(task.getEpicId());
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
}
