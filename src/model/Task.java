package model;

import util.Status;

import java.util.Objects;

public class Task {

    static int idCounter;
    private final int id;
    private String name;
    private String description;
    private Status status;


    public Task(String name, String description) {
        this.id = ++idCounter;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    protected Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public String toString() {
        return "Задача:" + "\n" +
                "Название: " + name + "\n" +
                "Описание: " + description + "\n" +
                "Статус: " + status + "\n";
    }

    public static int getId(int id) {
        return ++id;
    }
}