package data_structures

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.util.Date

@Serializable
data class Aspect(
    @SerialName("_id") val id: String,
    val name: String,
    val description: String?,
    val admin: List<String>,
    val members: List<String>?,
    val posts: List<String>?,
    val awards: List<String>?,
    val competitions: List<String>?,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date?,
    val colorScheme: ColorScheme?,
    val aspectGoal: AspectGoal?
)

@Serializable
data class ColorScheme(
    val primary: String?,
    val secondary: String?
)

@Serializable
data class AspectGoal(
    val goal: String?,
    val progress: Double?
)
