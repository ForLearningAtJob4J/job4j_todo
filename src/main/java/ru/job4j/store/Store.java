package ru.job4j.store;

import ru.job4j.model.Task;

import java.util.List;

public interface Store {
    List<Task> findAllTasks();

    Task add(Task task);

    void update(Task task);

    void delete(Integer id);

    Task findById(Integer id);
}