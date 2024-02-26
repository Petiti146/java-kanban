package ru.yandex.practicum.tasktracker.service.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Epic extends Task {

    private final Set<Integer> subTaskIds = new HashSet<>();


    public Epic(Task task) {
        super(task.getId(), task.getName(), task.getDescription());
    }

    public Set<Integer> getSubTaskIds() {
        return new HashSet<>(subTaskIds);
    }

    public boolean addSubTaskId(int subTaskId) {
        return subTaskIds.add(subTaskId);
    }

    public boolean removeSubTaskId(int subTaskId) {
        return subTaskIds.remove(subTaskId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds);
    }

    @Override
    public String toString() {
        return "Эпик: " + "\n" +
                "Название: " + getName() + "\n" +
                "Описание: " + getDescription() + "\n" +
                "Статус: " + getStatus();
    }
}