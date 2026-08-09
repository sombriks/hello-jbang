package app;

import app.models.Todo;
import app.services.TodoSvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class AppTest {

    @Autowired
    private TodoSvc todoSvc;

    @Test
    public void deveBuscarDadosIniciaisDoLiquibaseViaService() {
        List<Todo> todos = todoSvc.listarTodos();
        assertThat(todos, is(notNullValue()));
        assertThat(todos.size(), is(greaterThanOrEqualTo(2)));

        String primeiroTitulo = todos.get(0).getTitulo();
        assertThat(primeiroTitulo, containsString("Estudar JBang"));
    }
}
