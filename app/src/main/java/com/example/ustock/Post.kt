package com.example.ustock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        val str = formatter.format(value.toInstant())
        encoder.encodeString(str)
    }

    override fun deserialize(decoder: Decoder): Date {
        val str = decoder.decodeString()
        val instant = Instant.parse(str)
        return Date.from(instant)
    }
}



@Serializable
data class Post(
    @SerialName("_id")
    val id: String,
    val caption: String,
    val media: Media,
    val user: String,
    val aspects: List<String>?,
    val upvotes: List<String>?,
    val downvotes: List<String>?,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val updatedAt: Date?,
    val comments: List<String>?,
    val tags: List<String>?,
    val mentions: List<String>?
)

@Serializable
data class Media(
    val type: String,
    val content: String?,
    val url: String?
)
