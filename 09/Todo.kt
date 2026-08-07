import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

data class Todo(
    var id: Int? = null,
    var description: String,
    var completed: Boolean = false
)

object Todos : IntIdTable("todos") {
    val description = varchar("description", 255)
    val completed = bool("completed").default(false)
}
