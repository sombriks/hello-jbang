package app.services;

import app.models.Todo;
import app.repositories.TodoRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TodoSvc {

    @Inject
    private TodoRepo repository;

    public List<Todo> listarTodas() {
        return repository.findAll();
    }

    @Transactional
    public Todo salvar(Todo todo) {
        todo.setConcluido(false);
        return repository.save(todo);
    }
}
