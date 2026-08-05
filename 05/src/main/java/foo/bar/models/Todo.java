package foo.bar.models;

import io.vertx.sqlclient.Row;

public record Todo(Long id, String title, Boolean completed) {
    public static Todo map(Row row) {
        return new Todo(
                row.get(Long.class, "id"),
                row.get(String.class, "title"),
                row.get(Boolean.class, "completed")
        );
    }
}
