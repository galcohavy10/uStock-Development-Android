package data_structures

//Used to hold a date decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date


@OptIn(ExperimentalSerializationApi::class)
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
