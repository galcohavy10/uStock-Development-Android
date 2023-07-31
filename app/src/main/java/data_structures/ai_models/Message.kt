package data_structures.ai_models

import data_structures.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Message(
    val sender: String,
    val content: String,
    @SerialName("timestamp")
    @Serializable(with = DateSerializer::class)
    val timestamp: Date
)
