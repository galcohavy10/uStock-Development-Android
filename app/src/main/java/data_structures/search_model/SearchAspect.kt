package data_structures.search_model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchAspect(
    @SerialName("aspectID") val id: String,
    val aspectName: String,
    val aspectDescription: String
)
