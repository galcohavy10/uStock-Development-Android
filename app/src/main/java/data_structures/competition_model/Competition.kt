package data_structures.competition_model
import data_structures.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Competition(
    @SerialName("_id") val id: String,
    val name: String,
    val description: String?,
    @SerialName("goalType") val goalType: String,
    @SerialName("competitionGoal") val competitionGoal: String?,
    @SerialName("timeConstraint") val timeConstraint: Int?,
    @Serializable(with = DateSerializer::class)
    @SerialName("endDate") val endDate: Date?,
    @SerialName("participantsModel") val participantsModel: String,
    val participants: List<String>
)

@Serializable
data class CompetitionResponse(
    val competition: Competition
)
