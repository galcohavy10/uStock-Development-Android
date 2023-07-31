package data_structures

import data_structures.DateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.util.Date

@Serializable
data class Comment(
    @SerialName("_id") val id: String,
    val author: String,
    val content: String,
    val post: String,
    @Serializable(with = DateSerializer::class)
    val timestamp: Date,
    val upvotes: List<String>,
    val downvotes: List<String>,
    val replies: List<String>
)
