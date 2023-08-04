package data_structures.wallet_model
import data_structures.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable()
data class Transaction(
    @SerialName("_id") var id: String,
    val sender: String,
    val recipient: String,
    val amount: Double,
    @SerialName("stockPercentagePurchased") val stockPercentagePurchased: Double?,
    @Serializable(with = DateSerializer::class) val date: Date,
    val type: String,
    val stock: String
)
