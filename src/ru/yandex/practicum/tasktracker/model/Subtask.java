package ru.yandex.practicum.tasktracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicId;


    public Subtask(String name, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
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
                getDuration() + "," +
                getStartTime() + "," +
                getEpicId();
    }
}
