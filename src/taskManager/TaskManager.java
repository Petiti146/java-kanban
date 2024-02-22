package taskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();

    public Task addTask(Task task) {
        return tasks.put(task.getId(), task);
    }

    public SubTask addSubTask(SubTask subTask) {
        Task taskGoToEpic;
        taskGoToEpic = tasks.get(subTask.getParentId());
        Epic epic = new Epic(taskGoToEpic.getName(), taskGoToEpic.getDescription());
        epic.setId(taskGoToEpic.getId());
        subTask.setParentId(epic.getId());
        tasks.put(epic.getId(), epic);
        tasks.put(subTask.getId(), subTask);
        epic.getSubTaskIds().add(subTask.getId());
        return subTask;
    }

    private boolean isTaskExist(int id) {
        return tasks.containsKey(id);
    }

    public Task updateTask(Task task) {
        if (!isTaskExist(task.getId())) {
            System.out.println("Задача с таким id не существует");//для самопроверки все принтлн
            return null;
        }
        Task oldTask = tasks.get(task.getId());
        oldTask.setId(task.getId());
        oldTask.setName(task.getName());
        oldTask.setDescription(task.getDescription());

        if (!(oldTask instanceof Epic)) {
            oldTask.setStatus(task.getStatus());
        }
        return tasks.put(oldTask.getId(), oldTask);
    }

    public SubTask updateSubTask(SubTask subTask) {
        if (!isTaskExist(subTask.getParentId())) {
            System.out.println("Подзадача с таким id не существует");
            return null;
        }

        Task task = tasks.get(subTask.getId());

        if (!(task instanceof SubTask)) {
            System.out.println("Это айди не подзадачи");
            return null;
        }

        SubTask oldSubTask = (SubTask) task;
        oldSubTask.setName(subTask.getName());
        oldSubTask.setDescription(subTask.getDescription());
        oldSubTask.setStatus(subTask.getStatus());

        boolean isParentIdChanged = subTask.getParentId() != oldSubTask.getParentId();
        oldSubTask.setParentId(subTask.getParentId());

        Epic epic = (Epic) tasks.get(subTask.getParentId());

        if (isParentIdChanged) {
            epic.getSubTaskIds().remove(oldSubTask.getId());
            //TODO: ПРОВЕРИТЬ ЕСТЬ ЛИ ТАКОЙ ЭПИК
            Epic newEpic = (Epic) tasks.get(subTask.getParentId());
            epic.getSubTaskIds().add(subTask.getId());
            tasks.put(newEpic.getId(), newEpic);
        }

        List<SubTask> subTaskList = new ArrayList<>();
        int finishedTaskCounter = 0;

        for (Task t : tasks.values()) {
            if (!(t instanceof SubTask subTaskOfEpic)) {
                continue;
            }

            if (subTaskOfEpic.getParentId() == null || !subTaskOfEpic.getParentId().equals(epic.getId())) {
                continue;
            }
            if (Status.DONE.equals(subTaskOfEpic.getStatus())) {
                finishedTaskCounter++;
            }
            if (Status.IN_PROGRESS.equals(subTaskOfEpic.getStatus())) {
                epic.setStatus(Status.IN_PROGRESS);
            }
            subTaskList.add(subTaskOfEpic);
        }

        if (finishedTaskCounter == subTaskList.size()) {
            epic.setStatus(Status.DONE);
            tasks.put(epic.getId(), epic);
        }

        if (subTaskList.isEmpty()) {
            Task taskFromEpic = new Task(epic.getName(), epic.getDescription());
            tasks.remove(epic.getId());
            tasks.put(taskFromEpic.getId(), taskFromEpic);
        }

        return (SubTask) tasks.put(oldSubTask.getId(), oldSubTask);
    }

    public boolean deleteTaskById(int id) {
        return tasks.remove(id) != null;
    }

    public boolean deleteSubTaskById(int id) {
        SubTask subTask = (SubTask) tasks.get(id);
        if (subTask.getParentId() == null) {
            System.out.println("Подзадачи с таким айди не существует, у него отсутсвует parentId");
            return false;
        }
        if (!tasks.containsKey(subTask.getParentId())) {
            System.out.println("Эпик с таким айди не существует");
            return false;
        }

        if (!tasks.containsKey(id)) {
            System.out.println("Подзадачи с таким айди не существует");
            return false;
        }
        tasks.remove(id);

        Epic epic = (Epic) tasks.get(subTask.getParentId());
        List<SubTask> subTaskList = new ArrayList<>();
        int finishedTaskCounter = 0;

        for (Task task : tasks.values()) {
            if (!(task instanceof SubTask subTaskOfEpic)) {
                continue;
            }

            if (subTaskOfEpic.getParentId() == null || !subTaskOfEpic.getParentId().equals(epic.getId())) {
                continue;
            }
            if (Status.DONE.equals(subTaskOfEpic.getStatus())) {
                finishedTaskCounter++;
            }
            subTaskList.add(subTaskOfEpic);
        }

        if (finishedTaskCounter == subTaskList.size()) {
            epic.setStatus(Status.DONE);
            tasks.put(epic.getId(), epic);
        }

        if (subTaskList.isEmpty()) {
            Task taskFromEpic = new Task(epic.getName(), epic.getDescription());
            taskFromEpic.setId(epic.getId());
            tasks.remove(epic.getId());
            tasks.put(taskFromEpic.getId(), taskFromEpic);
        }

        epic.getSubTaskIds().remove(id);
        tasks.put(epic.getId(), epic);

        return tasks.remove(id) == null;
    }

    public boolean deleteEpicById(int id) {
        for (Task t : tasks.values()) {
            if (!(t instanceof SubTask subTaskOfEpic)) {
                continue;
            }

            if (subTaskOfEpic.getParentId() == null || !subTaskOfEpic.getParentId().equals(id)) {
                continue;
            }
            tasks.remove(t.getId());
        }
        return tasks.remove(id) == null;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public SubTask readSubTaskById(int id) {
        if (tasks.get(id) == null) {
            System.out.println("Подзадачи под таким id не существует");
            return null;
        }
        tasks.get(id).toString();
        return (SubTask) tasks.get(id);
    }

    public Epic readEpicById(int id) {
        if (tasks.get(id) == null) {
            System.out.println("Эпика под таким id не существует");
            return null;
        }

        if (tasks.get(id) instanceof Epic epic) {
            return epic;
        }
        return null;
    }

    public Map<Integer, Task> readTasks() {
        return this.tasks;
    }

    public void deleteAllEpics() {
        for (Task task : this.tasks.values()) {
            if (task instanceof Epic epic) {
                deleteEpicById(epic.getId());
            }
        }
    }

    public void deleteAllTasks() {
        for (Task task : this.tasks.values()) {
            if (task instanceof SubTask tasks1) {
                deleteEpicById(tasks1.getId());
            }
        }
    }

    public void deleteAllSubTask() {
        for (Task task : this.tasks.values()) {
            if (task instanceof SubTask subTask) {
                deleteEpicById(subTask.getId());
            }
        }
    }

    public void displayTasks() {//для самопроверки
        for (Task task : tasks.values()) {
            if (task instanceof SubTask task1) {
                System.out.printf("%d. %s (%s)%n", task.getId(), task.getName(), task.getStatus());
            }
        }
    }
}
