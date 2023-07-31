package data_structures.wallet_model
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

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
    val date: Date,
    val value: Double
)

object StockDataPointSerializer : KSerializer<StockDataPoint> {
    //TODO: Figure out if this works
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("StockDataPoint") {
        element<String>("date")
        element<Double>("value")
    }


    override fun deserialize(decoder: Decoder): StockDataPoint {
        val dec: CompositeDecoder = decoder.beginStructure(descriptor)
        var date: Date? = null
        var value: Double = 0.0

        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> date = Date(dec.decodeStringElement(descriptor, index))
                1 -> value = dec.decodeDoubleElement(descriptor, index)
                else -> throw SerializationException("Unknown index $index")
            }
        }

        return StockDataPoint(
            date ?: throw SsManifestParser.MissingFieldException("date"),
            value
        )
    }

    override fun serialize(encoder: Encoder, value: StockDataPoint) {
        val compositeOutput: CompositeEncoder = encoder.beginStructure(descriptor)
        compositeOutput.encodeStringElement(descriptor, 0, value.date.toString())
        compositeOutput.encodeDoubleElement(descriptor, 1, value.value)
        compositeOutput.endStructure(descriptor)
    }
}
