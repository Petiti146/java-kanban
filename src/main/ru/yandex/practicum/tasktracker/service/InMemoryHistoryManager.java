package ru.yandex.practicum.tasktracker.service;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private final HashMap<Integer, Node> map = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (map.get(task.getId()) != null) {
                removeNode(map.get(task.getId()));
            }
            linkLast(task);
        }
    }

    @Override
    public void linkLast(Task task) {
        Node newNode = new Node(task);
        if (map.get(task.getId()) != null) {
            Node currentNode = map.get(task.getId());
            Node nextNode = currentNode.getNext();
            Node lastNode = currentNode.getPrev();
            newNode.setPrev(lastNode);
            lastNode.setNext(nextNode);
            map.remove(task.getId());
        }
        if (head == null) {
            head = newNode;
        } else {
            Node tempNode = head;
            while (tempNode.getNext() != null) {
                tempNode = tempNode.getNext();
            }
            tempNode.setNext(newNode);
            newNode.setPrev(tempNode);
        }
        map.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node currentNode = map.get(id);
        if (currentNode != null) {
            removeNode(currentNode);
        }
    }

    public void removeNode(Node node) {
        Node currentNode = map.get(node.getData().getId());
        if (currentNode.getPrev() != null) {
            currentNode.getPrev().setNext(currentNode.getNext());
        }
        if (currentNode.getNext() != null) {
            currentNode.getNext().setPrev(currentNode.getPrev());
        }
        map.remove(node.getData().getId());
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        for (Node node : map.values()) {
            history.add(node.getData());
        }
        return history;
    }

    private void remove(Node node) {
        if (map.get(node.getData().getId()) == null) {
            throw new RuntimeException("Такого элемента не существует");
        }
        Node currentNode = map.get(node.getData().getId());
        Node nextNode = currentNode.getNext();
        Node lastNode = currentNode.getPrev();
        nextNode.setPrev(lastNode);
        lastNode.setNext(nextNode);
        map.remove(node.getData().getId());
    }
}
