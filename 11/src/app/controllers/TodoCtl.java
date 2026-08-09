package app.controllers;

import app.models.Todo;
import app.services.TodoSvc;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoCtl {
    private final TodoSvc svc;

    public TodoCtl(TodoSvc svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Todo> obterTodos() {
        return svc.listarTodos();
    }

    @PostMapping
    public Todo criarNovo(@RequestBody Todo todo) {
        return svc.salvar(todo);
    }
}
