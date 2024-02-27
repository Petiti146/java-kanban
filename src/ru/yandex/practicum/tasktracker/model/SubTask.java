package ru.yandex.practicum.tasktracker.model;

import java.util.Objects;

public class SubTask extends Task {
    private final Integer epicId;


    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
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
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicId, subTask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Подзадача: " + "\n" +
                "Название: " + getName() + "\n" +
                "Описание: " + getDescription() + "\n" +
                "Статус: " + getStatus();
    }
}

