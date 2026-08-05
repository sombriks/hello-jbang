package foo.bar.services;

import io.vertx.core.Future;
import io.vertx.jdbcclient.JDBCPool;

public class TodoSvc {

    private final JDBCPool pool;

    public TodoSvc (JDBCPool pool) {
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
}
