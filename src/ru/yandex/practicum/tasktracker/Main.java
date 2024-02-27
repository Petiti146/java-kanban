package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.service.TaskManager;
import ru.yandex.practicum.tasktracker.model.TaskStatus;
import ru.yandex.practicum.tasktracker.model.SubTask;
import ru.yandex.practicum.tasktracker.model.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();
        Task task = new Task("Эпик 1", "прогать");
        SubTask subTask = new SubTask("Подзадача 1", "Люто прогать", 1);
        Task task2 = new Task("Эпик 2", "прогать");
        SubTask subTask2 = new SubTask("Подзадача 2", "Это ко второму", 3);
        Task task3 = new Task("Задача 3", "кушать");
        SubTask subTask3 = new SubTask("Подзадача 3", "Это ко третьему", 5);
        Task task4 = new Task("Задача 4", "работать");
        manager.addTask(task);
        manager.addSubTask(subTask);
        manager.addTask(task2);
        manager.addSubTask(subTask2);
        manager.addTask(task3);
        manager.addSubTask(subTask3);
        manager.addTask(task4);
        SubTask subTask4 = new SubTask("Подзадача 4", "Это ко первому", task.getId());
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.DONE);
        task4.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(subTask);
        manager.updateSubTask(subTask2);
        manager.updateTask(task4);
        System.out.println("////////////////внизу саб таск");
        System.out.println(manager.getSubTaskById(subTask.getId()));
        System.out.println("////////////////внизу эпик");
        System.out.println(manager.getEpicById(task.getId()));
        System.out.println("////////////////удаляю саб таск");
        manager.deleteSubTaskById(subTask.getId());
        System.out.println(manager.getEpicById(task.getId()));
        System.out.println("////////////////задача 1");
        System.out.println(manager.getTaskById(task.getId()));
        System.out.println("////////////////внизу добавляю саб таску");
        manager.addSubTask(subTask4);
        System.out.println(manager.getEpicById(task.getId()));
    }
}