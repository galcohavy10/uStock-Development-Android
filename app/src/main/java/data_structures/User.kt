package data_structures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class User(
    @SerialName("_id")
    var id: String,
    var privacy: String,
    var username: String,
    var email: String?,
    var phoneNumber: String?,
    var firstName: String?,
    var lastName: String?,
    @Serializable(with = DateSerializer::class)
    var dob: Date?,
    var password: String?, //apple and google users may not need a password anymore
    var appleUserToken: String?, //apple
    var friends: List<String>?,
    var wallet: String?,
    var investedUsers: List<String>?,
    var investedAspects: List<String>?,
    var stock: String?,
    var competitions: List<String>?,
    var blockedUsers: List<String>? //added to appease apple's mandatory blocking users feature
)
