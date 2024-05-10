package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.exeption.ManagerLoadException;
import ru.yandex.practicum.tasktracker.exeption.ManagerSaveException;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task addTask(Task task) throws IllegalArgumentException {
        Task savedTask = super.addTask(task);
        saveToFile();
        return savedTask;
    }

    @Override
    public Subtask addSubtask(Subtask subTask) {
        Subtask savedSubTask = super.addSubtask(subTask);
        saveToFile();
        return savedSubTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task savedTask = super.updateTask(task);
        saveToFile();
        return savedTask;
    }

    @Override
    public Subtask updateSubtask(Subtask subTask) {
        Subtask savedSubtask = super.updateSubtask(subTask);
        saveToFile();
        return savedSubtask;
    }

    @Override
    public boolean deleteTaskById(int id) {
        boolean isDeleted = super.deleteTaskById(id);
        saveToFile();
        return isDeleted;
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        boolean isDeleted = super.deleteSubtaskById(id);
        saveToFile();
        return isDeleted;
    }

    @Override
    public boolean deleteEpicById(int id) {
        boolean isDeleted = super.deleteEpicById(id);
        saveToFile();
        return isDeleted;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveToFile();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        saveToFile();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        saveToFile();
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id,type,name,status,description,duration,startTime,epic or endTime");
            for (Task task : this.getTasks()) {
                writer.println(task.toString());
            }
            for (Epic epic : this.getEpics()) {
                writer.println(epic.toString());
            }
            for (Subtask subtask : this.getSubtasks()) {
                writer.println(subtask.toString());
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден", e);
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка при сохранении", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            String stringFile = Files.readString(file.toPath());
            int max = -1;

            for (String string : stringFile.split("\n")) {
                List<String> fields = List.of(string.split(","));
                if (fields.get(0) == null) {
                    throw new NullPointerException("Файл пустой");
                }
                if (Integer.parseInt(fields.get(0)) > max) {
                    max = Integer.parseInt(fields.get(0));
                }
                switch (fields.get(1)) {
                    case "TASK":
                        Task savedTask = new Task(fields.get(2), fields.get(4), Duration.ofMinutes(Long.parseLong(fields.get(5))), LocalDateTime.parse(fields.get(6)));
                        savedTask.setStatus(TaskStatus.valueOf(fields.get(3)));
                        savedTask.setId(Integer.parseInt(fields.get(0)));
                        fileBackedTaskManager.tasks.put(savedTask.getId(), savedTask);
                        break;
                    case "EPIC":
                        Epic savedEpic = new Epic(fields.get(2), fields.get(4), Duration.ofMinutes(Long.parseLong(fields.get(5))), LocalDateTime.parse(fields.get(6)), LocalDateTime.parse(fields.get(7)));
                        savedEpic.setStatus(TaskStatus.valueOf(fields.get(3)));
                        savedEpic.setId(Integer.parseInt(fields.get(0)));
                        fileBackedTaskManager.epics.put(savedEpic.getId(), savedEpic);
                        break;
                    case "SUBTASK":
                        Subtask savedSubTask = new Subtask(fields.get(2), fields.get(4), Integer.parseInt(fields.get(5)), Duration.ofMinutes(Long.parseLong(fields.get(5))), LocalDateTime.parse(fields.get(6)));
                        savedSubTask.setStatus(TaskStatus.valueOf(fields.get(3)));
                        savedSubTask.setId(Integer.parseInt(fields.get(0)));
                        Epic epic = fileBackedTaskManager.getEpicById(savedSubTask.getEpicId());
                        epic.addSubTaskId(savedSubTask.getId());
                        fileBackedTaskManager.subTasks.put(savedSubTask.getId(), savedSubTask);
                        break;
                }
            }
            fileBackedTaskManager.counterId = max;
            return fileBackedTaskManager;
        } catch (NullPointerException e) {
            throw new ManagerLoadException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ManagerLoadException("Ошибка при загрузке", e);
        }
    }
}
