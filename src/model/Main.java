package model;

public class Main {

    public static void main(String[] args) {

        Task task1 = new Task("Task1", null);
        SubTask subTask1 = new SubTask("SubTask1", null, 1);

        Manager manager = new Manager();
        manager.addTask(task1);

        manager.addSubTask(task1, subTask1);

        manager.readEpicById(task1.getId());
        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);

        Epic epic = manager.readEpicById(task1.getId());
        System.out.println(epic);
        manager.deleteSubTaskById(subTask1.getId());
        System.out.println(manager.readSubTaskById(2));
        System.out.println(manager.readTaskById(task1.getId()));
        System.out.println(manager.readTasks());

        System.out.println(" ///////////////////////////////////////////////////////////////////////////");
        Task parametersForTaskToUpdate = new Task("Updated task1" , "Updated description");
        parametersForTaskToUpdate.setId(task1.getId());
        manager.updateTask(parametersForTaskToUpdate);
        System.out.println(manager.readTasks());
        System.out.println(manager.readEpicById(task1.getId()));
        manager.deleteEpicById(parametersForTaskToUpdate.getId());
        System.out.println("//////////////////////////////");
        System.out.println(manager.readTasks());
        int a = 0;
    }
}
