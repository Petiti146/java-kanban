package ru.yandex.practicum.tasktracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Epic extends Task {

    private final Set<Integer> subTaskIds = new HashSet<>();
    private LocalDateTime endTime;

    public Epic(Task task, Subtask subtask) {
        super(task.getName(), task.getDescription(), subtask.getDuration(), subtask.getStartTime());
        this.setId(task.getId());
        this.endTime = subtask.getEndTime();
    }

    public Epic(String epicName, String epicDescription, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(epicName, epicDescription, duration, startTime);
        this.endTime = endTime;
    }

    public Set<Integer> getSubTaskIds() {
        return new HashSet<>(subTaskIds);
    }

    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public boolean removeSubTaskId(int subTaskId) {
        return subTaskIds.remove(subTaskId);
    }

    public void removeAllSubTaskIds() {
        subTaskIds.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                TaskType.EPIC + "," +
                getName() + "," +
                getStatus() + "," +
                getDescription() + "," +
                getDuration() + "," +
                getStartTime() + "," +
                getEndTime();
    }


}
