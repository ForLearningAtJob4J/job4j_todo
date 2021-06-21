package ru.job4j.store;

import ru.job4j.model.IdOwner;
import ru.job4j.model.Task;
import ru.job4j.model.User;

import java.util.List;

public interface Store {
    List<Task> findAllTasks();

    User findUserByEmail(String email);

    <T> T add(T subject);

    <T> void update(T subject);

    <T extends IdOwner> void delete(T subject);

    <T extends IdOwner> T findById(T subject);
}