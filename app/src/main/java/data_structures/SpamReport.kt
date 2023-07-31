package data_structures

import java.util.Date

data class SpamReport(
    val reporter: String,
    val spamContent: String,
    val reportedUser: String,
    val reportedPost: String,
    val reportDate: Date?,
    val resolutionDate: Date?,
    val status: String
)
