package xpto.baz;

import jakarta.persistence.*;

@Entity
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Boolean done;

    @ManyToOne(optional = false)
    @JoinColumn(name = "todo_list_id")
    private TodoList todoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    public TodoItem() {
    }

    public TodoItem(TodoList todoList, String task) {
        this.todoList = todoList;
        this.description = task;
    }

    @Override
    public String toString() {
        return "(#" + id + ", " + description + ")";
    }
}

