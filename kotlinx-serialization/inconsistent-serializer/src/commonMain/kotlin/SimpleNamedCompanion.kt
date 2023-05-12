import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(SimpleNamedCompanion.Named::class)
data class SimpleNamedCompanion(val string: String) {
    companion object Named : KSerializer<SimpleNamedCompanion> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SimpleNamedCompanion", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): SimpleNamedCompanion =
            SimpleNamedCompanion(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: SimpleNamedCompanion) {
            encoder.encodeString(value.string)
        }
    }
}