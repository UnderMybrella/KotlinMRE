import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable(SealedInterfaceExplicitObject.Serializer::class)
sealed interface SealedInterfaceExplicitObject {
    abstract val name: String
    abstract val kind: Int

    @Serializable
    data class StringType(override val name: String, override val kind: Int, val data: String): SealedInterfaceExplicitObject
    @Serializable
    data class UnknownType(override val name: String, override val kind: Int, val data: JsonElement): SealedInterfaceExplicitObject

    public object Serializer : JsonContentPolymorphicSerializer<SealedInterfaceExplicitObject>(SealedInterfaceExplicitObject::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<SealedInterfaceExplicitObject> =
            when (((element as? JsonObject)?.get("kind") as? JsonPrimitive)?.intOrNull) {
                0 -> StringType.serializer()
                else -> UnknownType.serializer()
            }
    }
}