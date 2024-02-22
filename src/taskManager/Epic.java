package taskManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Epic extends Task {

    private Set<Integer> subTaskIds = new HashSet<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Set<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(Set<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
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
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
