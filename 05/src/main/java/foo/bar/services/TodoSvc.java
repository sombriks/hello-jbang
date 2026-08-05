package foo.bar.services;

import foo.bar.models.Todo;
import io.vertx.core.Future;
import io.vertx.jdbcclient.JDBCPool;

import java.util.ArrayList;
import java.util.List;

public class TodoSvc {

    private final JDBCPool pool;

    public TodoSvc(JDBCPool pool) {
        this.pool = pool;
    }

    public Future<Void> init() {
        String sql = """
                CREATE TABLE IF NOT EXISTS todos (
                    id integer PRIMARY KEY auto_increment,
                    title VARCHAR(255) NOT NULL,
                    completed BOOLEAN DEFAULT FALSE
                )
                """;
        return pool.query(sql)
                .execute()
                .mapEmpty();
    }

    public Future<List<Todo>> list() {
        String sql = """
                SELECT id, title, completed
                FROM todos
                ORDER BY id
                """;
        return pool.query(sql)
                .execute()
                .map(rows -> {
                    List<Todo> list = new ArrayList<>();
                    for (var row : rows)
                        list.add(Todo.map(row));
                    return list;
                });
    }
}
