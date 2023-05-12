import io.kotest.core.spec.style.FunSpec
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.protobuf.ProtoBuf

interface SerialFormatSpec<T : SerialFormat> {
    val format: T
}

abstract class SerialFormatFunSpec<T : SerialFormat>(
    override val format: T,
    body: SerialFormatFunSpec<T>.() -> Unit = {},
) : FunSpec({}), SerialFormatSpec<T> {
    init {
        body()
    }
}

abstract class JsonFunSpec(json: Json = Json, body: SerialFormatFunSpec<Json>.() -> Unit) :
    SerialFormatFunSpec<Json>(json, body)
@OptIn(ExperimentalSerializationApi::class)
abstract class ProtoBufFunSpec(protobuf: ProtoBuf = ProtoBuf, body: SerialFormatFunSpec<ProtoBuf>.() -> Unit) :
    SerialFormatFunSpec<ProtoBuf>(protobuf, body)

@OptIn(ExperimentalSerializationApi::class)
abstract class CborFunSpec(cbor: Cbor = Cbor, body: SerialFormatFunSpec<Cbor>.() -> Unit) :
    SerialFormatFunSpec<Cbor>(cbor, body)

@OptIn(ExperimentalSerializationApi::class)
abstract class PropertiesFunSpec(props: Properties = Properties, body: SerialFormatFunSpec<Properties>.() -> Unit) :
    SerialFormatFunSpec<Properties>(props, body)