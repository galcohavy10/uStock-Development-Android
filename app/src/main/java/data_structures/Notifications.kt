package data_structures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Notification(
    @SerialName("_id")
    val id: String,
    val recipient: String,
    val sender: String,
    val message: String,
    val read: Boolean,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date
)
