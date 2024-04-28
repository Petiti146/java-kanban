package ru.yandex.practicum.tasktracker.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Epic extends Task {

    private final Set<Integer> subTaskIds = new HashSet<>();

    public Epic(Task task) {
        super(task.getId(), task.getName(), task.getDescription());
    }

    public Epic(String epicName, String epicDescription) {
        super(epicName, epicDescription);
    }

    public Set<Integer> getSubTaskIds() {
        return new HashSet<>(subTaskIds);
    }

    public void addSubTaskId(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void removeSubTaskId(int subTaskId) {
        subTaskIds.remove(subTaskId);
    }

    public void removeAllSubTaskIds() {
        subTaskIds.clear();
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
                getDescription();
    }
}
