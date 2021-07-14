package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.User;

import javax.persistence.NoResultException;
import java.sql.SQLException;
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
    public <T> List<T> findAll(Class<T> cls) {
        return this.tx(
                session -> session.createQuery("from " + cls.getName(), cls).list()
        );
    }

    @Override
    public <T> List<T> findAllFetched(Class<T> cls, String prop) {
        return this.tx(
                session -> session.createQuery("select distinct tbl from " + cls.getName() + " tbl left join fetch tbl." + prop, cls).list()
        );
    }

    @Override
    public User findUserByEmail(String email) {
        return this.tx(
                session -> {
                    try {
                        return session.createQuery("from User where email = :email", User.class).setParameter("email", email).getSingleResult();
                    } catch (NoResultException nre) {
                        return null;
                    }
                }
        );
    }

    @Override
    public <T> T add(T subject) throws SQLException {
        this.tx(session -> session.save(subject.getClass().getName(), subject));
        return subject;
    }

    @Override
    public <T> void update(T subject) throws SQLException {
        this.tx(session -> {
            session.update(subject.getClass().getName(), subject);
            return null;
        });
    }

    @Override
    public <T> void deleteById(Class<T> cls, int id) {
        this.tx(session -> {
            T found = session.get(cls, id);
            session.delete(found);
            return found;
        });
    }

    @Override
    public <T> T getById(Class<T> cls, int id) {
        return this.tx(
                session -> session.get(cls, id)
        );
    }
}