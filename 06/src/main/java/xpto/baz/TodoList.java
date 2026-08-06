package xpto.baz;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToMany(mappedBy = "todoList", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<TodoItem> items = new HashSet<>();

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

    public Set<TodoItem> getItems() {
        return items;
    }

    public void setItems(Set<TodoItem> items) {
        this.items = items;
    }

    public TodoList(){}

    public TodoList(String description){
        this.description = description;
    }

}
