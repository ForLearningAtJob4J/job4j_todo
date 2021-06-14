package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Task;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreHbnStore implements Store {
    private static final Logger LOGGER = Logger.getLogger(PostgreHbnStore.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new PostgreHbnStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public List<Task> findAllTasks() {
        List<Task> result = null;
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            result = session.createQuery("from Task order by created").list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Task add(Task task) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            session.close();
            return task;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void update(Task task) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            Task task = new Task();
            task.setId(id);
            session.delete(task);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public Task findById(Integer id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Task result = session.get(Task.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }
}