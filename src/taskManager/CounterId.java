package taskManager;

public  class CounterId { //с самого начала был этот класс, хочу его оставить, знаю что разницы нет
    private static int id;
    public static int getId () {
        return ++id;
    }
}
