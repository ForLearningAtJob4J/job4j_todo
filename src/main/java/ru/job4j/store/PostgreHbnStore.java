package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Task;

import java.util.*;
import java.util.function.Function;
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

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Task> findAllTasks() {
        return this.tx(
                session -> session.createQuery("from Task order by created", Task.class).list()
        );
    }

    @Override
    public Task add(Task task) {
        this.tx(session -> session.save(task));
        return task;
    }

    @Override
    public void update(Task task) {
        this.tx(session -> {
            session.update(task);
            return null;
        });
    }

    @Override
    public void delete(Integer id) {
        this.tx(session -> {
            session.delete(session.get(Task.class, id));
            return null;
        });
    }

    @Override
    public Task findById(Integer id) {
        return this.tx(
                session -> session.find(Task.class, id)
        );
    }
}