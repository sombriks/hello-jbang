// TodoRes.java
package app.controllers;

import app.models.Todo;
import app.services.TodoSvc;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("todos")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoRes {

    @Inject
    private TodoSvc service;

    @GET
    public List<Todo> list() {
        return service.listarTodas();
    }

    @POST
    public Todo create(Todo todo) {
        return service.salvar(todo);
    }
}
