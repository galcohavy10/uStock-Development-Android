package data_structures.wallet_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    @SerialName("_id") val id: String,
    val balance: Double,
    val transactions: List<String>,
    val user: String
)
