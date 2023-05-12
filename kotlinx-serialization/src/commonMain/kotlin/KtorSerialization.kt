import kotlinx.serialization.*
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Ktor type information.
 * @property type: source KClass<*>
 * @property reifiedType: type with substituted generics
 * @property kotlinType: kotlin reified type with all generic type parameters.
 */
public data class TypeInfo(
    public val type: KClass<*>,
    public val kotlinType: KType? = null
)

/**
 * Returns [TypeInfo] for the specified type [T]
 */
@OptIn(ExperimentalStdlibApi::class)
public inline fun <reified T> typeInfo(): TypeInfo {
    val kType = typeOf<T>()
    return TypeInfo(T::class, kType)
}


@ExperimentalSerializationApi
@InternalSerializationApi
public fun SerializersModule.serializerForTypeInfo(typeInfo: TypeInfo): KSerializer<*> {
    val module = this
    return typeInfo.kotlinType
        ?.let { type ->
            if (type.arguments.isEmpty()) {
                null // fallback to a simple case because of
                // https://github.com/Kotlin/kotlinx.serialization/issues/1870
            } else {
                module.serializerOrNull(type)
            }
        }
        ?: module.getContextual(typeInfo.type)?.maybeNullable(typeInfo)
        ?: typeInfo.type.serializer().maybeNullable(typeInfo)
}

private fun <T : Any> KSerializer<T>.maybeNullable(typeInfo: TypeInfo): KSerializer<*> {
    return if (typeInfo.kotlinType?.isMarkedNullable == true) this.nullable else this
}