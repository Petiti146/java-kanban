import ru.yandex.practicum.tasktracker.service.model.SubTask;
import ru.yandex.practicum.tasktracker.service.model.Task;
import ru.yandex.practicum.tasktracker.service.TaskManager;
import ru.yandex.practicum.tasktracker.service.model.Status;

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
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        subTask.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE);
        task4.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask);
        manager.updateSubTask(subTask2);
        manager.updateTask(task4);

        System.out.println("////////////////");
        //   manager.displayEpics();
        System.out.println("////////////////");
        //  manager.displaySubTasks();
        System.out.println("////////////////");
        //   manager.displayTasks();
        System.out.println("////////////////");
        manager.deleteSubTaskById(2);
        manager.displayTasks();
        manager.displayEpics();
        manager.displaySubTasks();
    }
}