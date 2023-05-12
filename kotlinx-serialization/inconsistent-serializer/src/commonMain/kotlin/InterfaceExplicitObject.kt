import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable(InterfaceExplicitObject.Serializer::class)
interface InterfaceExplicitObject {
    abstract val name: String
    abstract val kind: Int

    @Serializable
    data class StringType(override val name: String, override val kind: Int, val data: String): InterfaceExplicitObject
    @Serializable
    data class UnknownType(override val name: String, override val kind: Int, val data: JsonElement): InterfaceExplicitObject

    public object Serializer : JsonContentPolymorphicSerializer<InterfaceExplicitObject>(InterfaceExplicitObject::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<InterfaceExplicitObject> =
            when (((element as? JsonObject)?.get("kind") as? JsonPrimitive)?.intOrNull) {
                0 -> StringType.serializer()
                else -> UnknownType.serializer()
            }
    }
}