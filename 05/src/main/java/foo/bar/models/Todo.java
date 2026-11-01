package foo.bar.models;

import io.vertx.sqlclient.Row;

public record Todo(Long id, String title, Boolean completed) {
    public static Todo map(Row row) {
        // testing classpath from intellij
        var x = new foo.bar.models.X(1);
        // valid, jbang worsk just fine, but intellij complains
        // var y = new X(2);
        return new Todo(
                row.get(Long.class, "id"),
                row.get(String.class, "title"),
                row.get(Boolean.class, "completed")
        );
    }
}
