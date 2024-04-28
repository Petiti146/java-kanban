package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.service.FileBackedTaskManager;
import ru.yandex.practicum.tasktracker.service.InMemoryTaskManager;
import ru.yandex.practicum.tasktracker.service.TaskManager;
import ru.yandex.practicum.tasktracker.model.TaskStatus;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        TaskManager manager = new FileBackedTaskManager(File.createTempFile("File1" , ".csv"));
        Task task = new Task("Epic 1", "work");
        Subtask subTask = new Subtask("Подзадача 1", "hard work", 1);
        Task task2 = new Task("Epic 2", "прогать");
        Subtask subtask2 = new Subtask("SubTask 2", "to 2 Epic", 3);
        Task task3 = new Task("Task 3", "кушать");
        Subtask subtask3 = new Subtask("Subtask 3", "to 3 Epic", 5);
        Task task4 = new Task("Task 4", "woek");
        manager.addTask(task);
        manager.addSubtask(subTask);
        manager.addTask(task2);
        manager.addSubtask(subtask2);
        manager.addTask(task3);
        manager.addSubtask(subtask3);
        manager.addTask(task4);
        Subtask subtask4 = new Subtask("Subtask 4", "to 1 epic", task.getId());
        manager.addSubtask(subtask4);
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.DONE);
        task4.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subTask);
        manager.updateSubtask(subtask2);
        manager.updateTask(task4);
        System.out.println("////////////////внизу саб таск");
        System.out.println(manager.getSubtaskById(subTask.getId()));
        System.out.println("////////////////внизу эпик");
        System.out.println(manager.getEpicById(task.getId()));
        System.out.println("////////////////удаляю саб таск");
        manager.deleteSubtaskById(subTask.getId());
        System.out.println(manager.getEpicById(task.getId()));
        System.out.println("////////////////задача 1");
        System.out.println(manager.getTaskById(task.getId()));
        System.out.println("////////////////внизу добавляю саб таску");
        manager.addSubtask(subtask4);
        System.out.println(manager.getEpicById(task.getId()));
        manager.deleteAllSubTasks();
        System.out.println(manager.getEpics());
    }

    /*private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }*/
}