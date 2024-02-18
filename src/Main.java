import model.Epic;
import model.Status;
import model.Task;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("TMS v. 1.0.0");

        System.out.println("Меню:");
        System.out.println("1. Список эпиков");
        System.out.println("2. Список задач");
        System.out.println("3. Показать эпика и его задачи");
        System.out.println("4. Показать определнную задачу");
        System.out.println("5. Создать эпик");
        System.out.println("6. Создать задачу");
        System.out.println("-1. Выход");

        Manager manager = new Manager();
        Task task;
        Epic epic;
        String name;
        String description;
        int epicId;
        int command = 0;

        while (command != -1) {
            task = null;
            epic = null;
            name = null;
            description = null;
            epicId = 0;

            System.out.println("Введите команду:");
            Scanner scanner = new Scanner(System.in);
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Вы ввели не число. Пожалуйста, введите снова.");
                continue;
            }
            switch (command) {
                case 1:
                    manager.displayEpics();
                    break;
                case 2:
                    manager.displayTasks();
                    break;
                case 3:
                    System.out.println("Введите индефикатор эпика:");
                    epic = manager.readEpicById(scanner.nextInt());
                    System.out.println(epic.toString());
                    break;
                case 4:
                    System.out.println("Введите индефикатор эпика:");
                    task = manager.readTaskById(scanner.nextInt());
                    System.out.println(task.toString());
                    break;
                case 5:
                    System.out.println("Введите данные эпика.");
                    System.out.println("Введите название:");
                    name = scanner.nextLine();
                    System.out.println("Введите описание");
                    description = scanner.nextLine();
                    Epic storedEpic = new Epic(name, description, Status.NEW);
                    manager.createEpic(storedEpic);
                    break;
                case 6:
                    System.out.println("Введите данные задачи.");
                    System.out.println("Введите название:");
                    name = scanner.nextLine();
                    System.out.println("Введите описание");
                    description = scanner.nextLine();
                    System.out.println("Введите индефикатор эпика или нажмите Enter:");
                    epicId = scanner.nextInt();
                    task = new Task(name, description, Status.NEW, epicId);
                    manager.createTask(task);
                    break;
            }
        }
        System.out.println("Завершение работы...");
    }
}
