import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.protobuf.ProtoBuf

interface SerialFormatSpec<T : SerialFormat> {
    val format: T
}

abstract class SerialFormatFlatSpec<T : SerialFormat>(
    override val format: T,
    body: SerialFormatFlatSpec<T>.() -> Unit = {},
) : FlatSpec({}), SerialFormatSpec<T> {
    init {
        body()
    }
}

abstract class JsonFlatSpec(json: Json = Json, body: SerialFormatFlatSpec<Json>.() -> Unit) :
    SerialFormatFlatSpec<Json>(json, body)
@OptIn(ExperimentalSerializationApi::class)
abstract class ProtoBufFlatSpec(protobuf: ProtoBuf = ProtoBuf, body: SerialFormatFlatSpec<ProtoBuf>.() -> Unit) :
    SerialFormatFlatSpec<ProtoBuf>(protobuf, body)

@OptIn(ExperimentalSerializationApi::class)
abstract class CborFlatSpec(cbor: Cbor = Cbor, body: SerialFormatFlatSpec<Cbor>.() -> Unit) :
    SerialFormatFlatSpec<Cbor>(cbor, body)

@OptIn(ExperimentalSerializationApi::class)
abstract class PropertiesFlatSpec(props: Properties = Properties, body: SerialFormatFlatSpec<Properties>.() -> Unit) :
    SerialFormatFlatSpec<Properties>(props, body)