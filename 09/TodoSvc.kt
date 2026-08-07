import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory

class TodoSvc {
    private val logger = LoggerFactory.getLogger(TodoSvc::class.java)

    fun init() {
        logger.info("Initializing TodoSvc")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Todos)
        }
        val sampleData = list()
        if (sampleData.isEmpty()) {
            create(Todo(description = "Feed the cat"))
            create(Todo(description = "Pay overdue bills"))
        }
    }

    fun list(): List<Todo> {
        logger.info("Listing todos")
        return transaction {
            return@transaction Todos
                .selectAll()
                .toList()
                .map {
                    Todo(
                        id = it[Todos.id].value,
                        description = it[Todos.description],
                        completed = it[Todos.completed]
                    )
                }
        }
    }

    fun create(todo: Todo): Todo {
        transaction {
            val result = Todos.insertAndGetId {
                it[Todos.description] = todo.description
                it[Todos.completed] = todo.completed
            }
            todo.id = result.value
        }
        return todo
    }
}
