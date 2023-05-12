import io.kotest.assertions.throwables.shouldNotThrowExactlyUnit
import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.assertions.withClue
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationException
import kotlinx.serialization.modules.SerializersModule

inline fun <T> SerialFormatSpec<*>.serialiser(
    wrap: (() -> Unit) -> Unit,
    crossinline block: SerializersModule.() -> T,
): T? {
    var serialiser: T? = null
    wrap {
        serialiser = format.serializersModule.block()
    }
    return serialiser
}

inline fun <T> SerialFormatSpec<*>.serialiserShouldThrowIllegalArgument(crossinline block: SerializersModule.() -> T): T? =
    serialiser({ shouldThrowExactlyUnit<IllegalArgumentException>(it) }, block)

inline fun <T> SerialFormatSpec<*>.serialiserShouldNotThrowIllegalArgument(crossinline block: SerializersModule.() -> T): T? =
    serialiser({ shouldNotThrowExactlyUnit<IllegalArgumentException>(it) }, block)

inline fun <F : SerialFormat, T> SerialFormatSpec<F>.shouldSerialise(block: F.() -> T): T? {
    var value: T? = null
    shouldNotThrowUnit<SerializationException> { value = format.block() }
    return value
}

inline fun <F : SerialFormat, T> SerialFormatSpec<F>.shouldSerialise(clue: Any?, block: F.() -> T): T? {
    var value: T? = null
    withClue(clue) { shouldNotThrowUnit<SerializationException> { value = format.block() } }
    return value
}