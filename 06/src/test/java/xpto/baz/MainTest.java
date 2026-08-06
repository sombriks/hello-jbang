package xpto.baz;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MainTest {

    final Main main = new Main();

    @Test
    void shouldCreateTodo() {
        var result = main.add("today", "doomscrolling");
        assertThat(result, is(not(nullValue())));
        assertThat(result.getId(), notNullValue());
        assertThat(result.getId(), greaterThan(0L));
    }

}
