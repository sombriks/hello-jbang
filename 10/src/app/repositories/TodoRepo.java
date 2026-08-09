package app.repositories;

import app.models.Todo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TodoRepo {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public List<Todo> findAll() {
        return em.createQuery("SELECT t FROM Todo t", Todo.class)
            .getResultList();
    }

    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            em.persist(todo);
            return todo;
        } else {
            return em.merge(todo);
        }
    }

    public Optional<Todo> findById(Long id) {
        Todo todo = em.find(Todo.class, id);
        return Optional.ofNullable(todo);
    }

    public void deleteById(Long id) {
        Todo todo = em.find(Todo.class, id);
        if (todo != null) {
            em.remove(todo);
        }
    }
}
