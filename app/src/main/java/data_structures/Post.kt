package data_structures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Post(
    @SerialName("_id")
    val id: String,
    val caption: String,
    val media: Media,
    val user: String,
    val aspects: List<String>?,
    val upvotes: List<String>?,
    val downvotes: List<String>?,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date?,
    val comments: List<String>?,
    val tags: List<String>?,
    val mentions: List<String>?
)

@Serializable
data class Media(
    val type: String,
    val content: String?,
    val url: String?
)

