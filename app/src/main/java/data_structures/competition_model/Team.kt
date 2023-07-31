package data_structures.competition_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    @SerialName("_id") val id: String,
    val name: String,
    val members: List<String>,
    val progress: Double
)
