package app.todos;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Properties;

public class TodoSvc {

    private final Jdbi jdbi;

    public TodoSvc() throws Exception {
        var cfg = new HikariConfig("/application.properties");
        var ds = new HikariDataSource(cfg);
        jdbi = Jdbi.create(ds);
    }

    public void init() {
        jdbi.useHandle(handle -> {
            handle.execute("""
                        CREATE TABLE IF NOT EXISTS todos (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            description VARCHAR(255) NOT NULL,
                            done BOOLEAN NOT NULL DEFAULT FALSE
                        )
                    """);
        });
    }

    public List<TodoMdl> list() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, description, done FROM todos")
                        .map(TodoMdl::fromRow)
                        .list()
        );
    }

    public TodoMdl save(TodoMdl todo) {
        return jdbi.withHandle(handle -> {
            if (todo.id() == null) {
                long generatedId = handle.createUpdate("""
                                    INSERT INTO todos (description, done) 
                                    VALUES (:description, :done)
                                """)
                        .bind("description", todo.description())
                        .bind("done", todo.done())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class)
                        .one();

                return new TodoMdl(generatedId, todo.description(), todo.done());
            } else {
                handle.createUpdate("""
                                    UPDATE todos 
                                    SET description = :description, done = :done 
                                    WHERE id = :id
                                """)
                        .bind("id", todo.id())
                        .bind("description", todo.description())
                        .bind("done", todo.done())
                        .execute();

                return todo;
            }
        });
    }
}
