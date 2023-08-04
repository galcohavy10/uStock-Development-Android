package data_structures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HelpTicket(
    @SerialName("_id")
    val userID: String,
    val issue: String
)