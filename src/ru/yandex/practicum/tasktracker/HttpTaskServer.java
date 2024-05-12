package ru.yandex.practicum.tasktracker;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tasktracker.handler.task.TaskHandler;
import ru.yandex.practicum.tasktracker.service.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(
                new InetSocketAddress("localhost", 8080),
                0
        );
        httpServer.createContext("/tasks", new TaskHandler(new InMemoryTaskManager()));
        httpServer.start();
    }
}
