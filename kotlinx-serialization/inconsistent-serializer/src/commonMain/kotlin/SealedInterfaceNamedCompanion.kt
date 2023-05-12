import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable(SealedInterfaceNamedCompanion.CompanionSerializer::class)
sealed interface SealedInterfaceNamedCompanion {
    abstract val name: String
    abstract val kind: Int

    @Serializable
    data class StringType(override val name: String, override val kind: Int, val data: String): SealedInterfaceNamedCompanion
    @Serializable
    data class UnknownType(override val name: String, override val kind: Int, val data: JsonElement): SealedInterfaceNamedCompanion

    public companion object CompanionSerializer : JsonContentPolymorphicSerializer<SealedInterfaceNamedCompanion>(SealedInterfaceNamedCompanion::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<SealedInterfaceNamedCompanion> =
            when (((element as? JsonObject)?.get("kind") as? JsonPrimitive)?.intOrNull) {
                0 -> StringType.serializer()
                else -> UnknownType.serializer()
            }
    }
}