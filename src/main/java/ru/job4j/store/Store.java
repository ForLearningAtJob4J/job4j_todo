package ru.job4j.store;

import ru.job4j.model.User;

import java.sql.SQLException;
import java.util.List;

public interface Store {
    <T> List<T> findAll(Class<T> cls);

    <T> List<T> findAllFetched(Class<T> cls, String prop);

    User findUserByEmail(String email);

    <T> T add(T subject) throws SQLException;

    <T> void update(T subject) throws SQLException;

    <T> void deleteById(Class<T> cls, int id);

    <T> T getById(Class<T> cls, int id);
}