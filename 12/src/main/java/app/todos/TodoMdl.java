package app.todos;

import org.jdbi.v3.core.statement.StatementContext;
import java.sql.ResultSet;
import java.sql.SQLException;

public record TodoMdl(Long id, String description, Boolean done) {

    public TodoMdl(String description) {
        this(null, description, false);
    }

    public TodoMdl(String description, Boolean done) {
        this(null, description, done);
    }

    public static TodoMdl fromRow(ResultSet rs, StatementContext ctx) throws SQLException {
        return new TodoMdl(
                rs.getLong("id"),
                rs.getString("description"),
                rs.getBoolean("done")
        );
    }
}
