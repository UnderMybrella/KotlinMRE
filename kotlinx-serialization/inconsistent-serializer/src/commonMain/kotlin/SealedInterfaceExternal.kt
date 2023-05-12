import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

sealed interface SealedInterfaceExternal<T> {
    val name: String
    val kind: Int
    val data: T

    @Serializable
    data class StringType(override val name: String, override val kind: Int, override val data: String): SealedInterfaceExternal<String>
    @Serializable
    data class UnknownType(override val name: String, override val kind: Int, override val data: JsonElement): SealedInterfaceExternal<JsonElement>

    public companion object CompanionSerializer : KSerializer<SealedInterfaceExternal<*>> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SealedInterfaceCompanion") {
            element<String>("name")
            element<Int>("kind")
            element<JsonElement>("data")
        }

        override fun deserialize(decoder: Decoder): SealedInterfaceExternal<*> {
            decoder as JsonDecoder

            val obj = decoder.decodeJsonElement().jsonObject

            return when (obj.getValue("kind").jsonPrimitive.int) {
                0 -> decoder.json.decodeFromJsonElement<StringType>(obj)
                else -> decoder.json.decodeFromJsonElement<UnknownType>(obj)
            }
        }

        override fun serialize(encoder: Encoder, value: SealedInterfaceExternal<*>) {
            when (value) {
                is StringType -> encoder.encodeSerializableValue(StringType.serializer(), value)
                is UnknownType -> encoder.encodeSerializableValue(UnknownType.serializer(), value)
            }
        }

    }
    public object ObjectSerializer : KSerializer<SealedInterfaceExternal<*>> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SealedInterfaceCompanion") {
            element<String>("name")
            element<Int>("kind")
            element<JsonElement>("data")
        }

        override fun deserialize(decoder: Decoder): SealedInterfaceExternal<*> {
            decoder as JsonDecoder

            val obj = decoder.decodeJsonElement().jsonObject

            return when (obj.getValue("kind").jsonPrimitive.int) {
                0 -> decoder.json.decodeFromJsonElement<StringType>(obj)
                else -> decoder.json.decodeFromJsonElement<UnknownType>(obj)
            }
        }

        override fun serialize(encoder: Encoder, value: SealedInterfaceExternal<*>) {
            when (value) {
                is StringType -> encoder.encodeSerializableValue(StringType.serializer(), value)
                is UnknownType -> encoder.encodeSerializableValue(UnknownType.serializer(), value)
            }
        }

    }
    public class NoArgsSerializer : KSerializer<SealedInterfaceExternal<*>> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SealedInterfaceCompanion") {
            element<String>("name")
            element<Int>("kind")
            element<JsonElement>("data")
        }

        override fun deserialize(decoder: Decoder): SealedInterfaceExternal<*> {
            decoder as JsonDecoder

            val obj = decoder.decodeJsonElement().jsonObject

            return when (obj.getValue("kind").jsonPrimitive.int) {
                0 -> decoder.json.decodeFromJsonElement<StringType>(obj)
                else -> decoder.json.decodeFromJsonElement<UnknownType>(obj)
            }
        }

        override fun serialize(encoder: Encoder, value: SealedInterfaceExternal<*>) {
            when (value) {
                is StringType -> encoder.encodeSerializableValue(StringType.serializer(), value)
                is UnknownType -> encoder.encodeSerializableValue(UnknownType.serializer(), value)
            }
        }

    }
    public class GenericSerializer(val ignored: KSerializer<*>) : KSerializer<SealedInterfaceExternal<*>> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SealedInterfaceCompanion") {
            element<String>("name")
            element<Int>("kind")
            element<JsonElement>("data")
        }

        override fun deserialize(decoder: Decoder): SealedInterfaceExternal<*> {
            decoder as JsonDecoder

            val obj = decoder.decodeJsonElement().jsonObject

            return when (obj.getValue("kind").jsonPrimitive.int) {
                0 -> decoder.json.decodeFromJsonElement<StringType>(obj)
                else -> decoder.json.decodeFromJsonElement<UnknownType>(obj)
            }
        }

        override fun serialize(encoder: Encoder, value: SealedInterfaceExternal<*>) {
            when (value) {
                is StringType -> encoder.encodeSerializableValue(StringType.serializer(), value)
                is UnknownType -> encoder.encodeSerializableValue(UnknownType.serializer(), value)
            }
        }

    }
}

typealias SealedInterfaceExternalCompanion<T> = @Serializable(SealedInterfaceExternal.CompanionSerializer::class) SealedInterfaceExternal<T>
typealias SealedInterfaceExternalObject<T> = @Serializable(SealedInterfaceExternal.ObjectSerializer::class) SealedInterfaceExternal<T>
typealias SealedInterfaceExternalNoArgs<T> = @Serializable(SealedInterfaceExternal.NoArgsSerializer::class) SealedInterfaceExternal<T>
typealias SealedInterfaceExternalGeneric<T> = @Serializable(SealedInterfaceExternal.GenericSerializer::class) SealedInterfaceExternal<T>