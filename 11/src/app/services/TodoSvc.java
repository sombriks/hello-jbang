package app.services;

import app.models.Todo;
import app.repositories.TodoRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TodoSvc {
    private final TodoRepo repo;

    public TodoSvc(TodoRepo repo) {
        this.repo = repo;
    }

    public List<Todo> listarTodos() {
        return repo.findAll();
    }

    public Todo salvar(Todo todo) {
        return repo.save(todo);
    }
}
