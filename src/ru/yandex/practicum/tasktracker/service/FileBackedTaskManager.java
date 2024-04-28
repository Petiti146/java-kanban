package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task addTask(Task task) {
        Task savedTask = super.addTask(task);
        save();
        return savedTask;
    }

    @Override
    public Subtask addSubtask(Subtask subTask) {
        Subtask savedSubTask = super.addSubtask(subTask);
        save();
        return savedSubTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic savedEpic = super.addEpic(epic);
        save();
        return savedEpic;
    }

    @Override
    public Task updateTask(Task task) {
        Task savedTask = super.updateTask(task);
        save();
        return savedTask;
    }

    @Override
    public Subtask updateSubtask(Subtask subTask) {
        Subtask savedSubtask = super.updateSubtask(subTask);
        save();
        return savedSubtask;
    }

    @Override
    public boolean deleteTaskById(int id) {
        boolean isDeleted = super.deleteTaskById(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        boolean isDeleted = super.deleteSubtaskById(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean deleteEpicById(int id) {
        boolean isDeleted = super.deleteEpicById(id);
        save();
        return isDeleted;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    public void save() {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id,type,name,status,description,epic");
            for (Task task : this.getTasks()) {
                writer.println(task.toString());
            }
            for (Epic epic : this.getEpics()) {
                writer.println(epic.toString());
            }
            for (Subtask subtask : this.getSubtasks()) {
                writer.println(subtask.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            String stringFile = Files.readString(file.toPath());
            int max = -1;
            for (String string : stringFile.split("\n")) {
                List<String> fields = List.of(string.split(","));
                if (Integer.parseInt(fields.get(0)) > max) {
                    max = Integer.parseInt(fields.get(0));
                }
                switch (fields.get(1)) {
                    case "TASK", "EPIC":
                        Task savedTask = new Task(fields.get(2), fields.get(4));
                        savedTask.setStatus(TaskStatus.valueOf(fields.get(3)));
                        savedTask.setId(Integer.parseInt(fields.get(0)));
                        fileBackedTaskManager.addTask(savedTask);
                        break;
                    case "SUBTASK":
                        Subtask savedSubTask = new Subtask(fields.get(2), fields.get(4), Integer.parseInt(fields.get(5)));
                        savedSubTask.setStatus(TaskStatus.valueOf(fields.get(3)));
                        savedSubTask.setId(Integer.parseInt(fields.get(0)));
                        fileBackedTaskManager.addTask(savedSubTask);
                        break;
                }
            }
            fileBackedTaskManager.setCounterId(max);
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }
}
