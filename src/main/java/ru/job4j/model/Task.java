package ru.job4j.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table (name = "item")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "\"desc\"")
    private String desc;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    final private List<Category> categories = new ArrayList<>();

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Task() {
        created = new Date(System.currentTimeMillis());
    }

    public Task(int id, String description, Date created, boolean done, User user) {
        this.id = id;
        this.desc = description;
        this.created = created;
        this.done = done;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Task setId(int id) {
        this.id = id;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Task setDesc(String description) {
        this.desc = description;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public Task setCreated(Date created) {
        this.created = created;
        return this;
    }

    public boolean isDone() {
        return done;
    }

    public Task setDone(boolean done) {
        this.done = done;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Task setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}