package ru.yandex.practicum.tasktracker.service;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message, Exception cause) {
        super(message, cause);
    }
}
