package model;

import java.util.Objects;

public class SubTask extends model.Task {
    private final Integer parentId;


    public SubTask(String name, String description, int parentId) {
        super(name, description);
        this.parentId = parentId;
    }

    public Integer getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(parentId, subTask.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentId);
    }

    @Override
    public String toString() {
        return "Подзадача: " +"\n" +
                "Название: " + getName() + "\n" +
                "Описание: " + getDescription() + "\n" +
                "Статус: " + getStatus();
    }
}

