package ru.yandex.practicum.tasktracker.exeption;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message, Exception cause) {
        super(message, cause);
    }
}