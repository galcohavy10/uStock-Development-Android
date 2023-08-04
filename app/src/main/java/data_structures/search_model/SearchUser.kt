package data_structures.search_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchUser(
    @SerialName("userID") val id: String,
    val username: String,
    val following: Boolean
)
