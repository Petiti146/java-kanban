package ru.yandex.practicum.tasktracker.handler;

import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractHandler implements HttpHandler {
    protected String getJsonString(InputStream inputStream) {
        String json = null;
        try {
            json = new String(inputStream.readAllBytes());
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
