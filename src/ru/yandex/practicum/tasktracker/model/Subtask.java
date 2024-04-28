package ru.yandex.practicum.tasktracker.model;

import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;


    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass().getSuperclass() != o.getClass().getSuperclass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(super.getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    @Override
    public String toString() {
        return getId() + "," +
                TaskType.SUBTASK + "," +
                getName() + "," +
                getStatus() + "," +
                getDescription() + "," +
                getEpicId();
    }
}
