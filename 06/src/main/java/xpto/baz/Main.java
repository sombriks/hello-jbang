package xpto.baz;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {

    private final EntityManagerFactory emf = Persistence //
            .createEntityManagerFactory("default");

    public List<TodoList> list() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("""
                    select t from TodoList t
                    """, TodoList.class).getResultList();
        }
    }

    public TodoList add(String list, String task) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TodoList todoList = new TodoList(list);
            TodoItem todoItem = new TodoItem(todoList, task);
            em.persist(todoList);
            em.persist(todoItem);
            em.flush();
            em.clear();
            em.getTransaction().commit();
            todoList.getItems().add(todoItem);
            return todoList;
        }
    }

    public void close() {
        emf.close();
    }

    public static void main(String... args) throws Exception {
        var app = new Main();
        app.add("today", "walk the dog");
        List<TodoList> result = app.list();
        result.forEach(IO::println);
        app.close();
    }
}
