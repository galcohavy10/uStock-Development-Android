package data_structures.ai_models
import data_structures.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class AIChat(
    @SerialName("_id") val id: String,
    val creator: String,
    val assistantName: String,
    val messages: List<Message>,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date
)
