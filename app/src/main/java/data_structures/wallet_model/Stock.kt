package data_structures.wallet_model
import data_structures.DateSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.*
@Serializable
data class Stock(
    val symbol: String,
    val wallet: String,
    @SerialName("_id")
    val id: String,
    val history: List<StockDataPoint>
)



@Serializable(with = StockDataPointSerializer::class)
data class StockDataPoint(
    @Serializable(with = DateSerializer::class)
    val date: Date,
    val value: Double
)

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = StockDataPoint::class)
object StockDataPointSerializer : KSerializer<StockDataPoint> {

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("StockDataPoint") {
        element<String>("date")
        element<Double>("value")
    }

    override fun serialize(encoder: Encoder, value: StockDataPoint) {
        val dateStr = dateFormatter.format(value.date.toInstant())
        val json = JsonObject(mapOf(
            "date" to JsonPrimitive(dateStr),
            "value" to JsonPrimitive(value.value)
        ))
        encoder.encodeSerializableValue(JsonElement.serializer(), json)
    }

    override fun deserialize(decoder: Decoder): StockDataPoint {
        val json = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonObject
        val dateStr = json["date"]!!.jsonPrimitive.content
        val value = json["value"]!!.jsonPrimitive.double

        val instant = Instant.parse(dateStr)
        val date = Date.from(instant)

        return StockDataPoint(date, value)
    }
}






//
//@Serializable(with = StockDataPointSerializer::class)
//data class StockDataPoint(
//    val date: String,
//    val value: Double
//)
//
//object StockDataPointSerializer : KSerializer<StockDataPoint> {
//    //TODO: Figure out if this works
//    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("StockDataPoint") {
//        element<String>("date")
//        element<Double>("value")
//    }
//
//
//    override fun deserialize(decoder: Decoder): StockDataPoint {
//        val dec: CompositeDecoder = decoder.beginStructure(descriptor)
//        var date: String? = null
//        var value: Double = 0.0
//
//
//        //TODO: Fix illegal arguement in the Date
//        loop@ while (true) {
//            when (val index = dec.decodeElementIndex(descriptor)) {
//                CompositeDecoder.DECODE_DONE -> break@loop
//                0 -> date = dec.decodeStringElement(descriptor, index)
//                1 -> value = dec.decodeDoubleElement(descriptor, index)
//                else -> throw SerializationException("Unknown index $index")
//            }
//        }
//
//        return StockDataPoint(
//            date ?: throw SsManifestParser.MissingFieldException("date"),
//            value
//        )
//    }
//
//    override fun serialize(encoder: Encoder, value: StockDataPoint) {
//        val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
//        compositeOutput.encodeStringElement(descriptor, 0, value.date.toString())
//        compositeOutput.encodeDoubleElement(descriptor, 1, value.value)
//        compositeOutput.endStructure(descriptor)
//    }
//}
