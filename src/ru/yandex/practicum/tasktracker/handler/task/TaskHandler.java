package ru.yandex.practicum.tasktracker.handler.task;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.tasktracker.handler.AbstractHandler;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandler extends AbstractHandler {

    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                getTasks(exchange);
                break;
            case "DELETE":
                deleteTask(exchange);
                break;
            case "POST":
                postTask(exchange);
                break;
        }
    }

    private void postTask(HttpExchange exchange) throws IOException {
        OutputStream stream = exchange.getResponseBody();
        InputStream inputStream = exchange.getRequestBody();

        String json = getJsonString(inputStream);

        JSONObject jsonObject = new JSONObject(json);
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        Duration duration = jsonObject.getString("duration");
        LocalDateTime startTime = jsonObject.getString("startTime");

        Task task = new Task(name, description, duration, startTime);

        if (id != 0) {
            task.setId(id);
            taskManager.updateTask(task);
        } else {
            taskManager.addTask(task);
        }

        exchange.sendResponseHeaders(201, 0);
        stream.flush();
        stream.close();
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        OutputStream stream = exchange.getResponseBody();
        List<Task> tasks = taskManager.getTasks();
        exchange.sendResponseHeaders(200, tasks.toString().length());
        stream.write(tasks.toString().getBytes());
        stream.flush();
        stream.close();
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        OutputStream stream = exchange.getResponseBody();
        exchange.sendResponseHeaders(200, 0);
        stream.flush();
        stream.close();
    }
}
