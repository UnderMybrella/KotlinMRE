import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon

@OptIn(ExperimentalSerializationApi::class)
abstract class HoconFunSpec(hocon: Hocon = Hocon, body: SerialFormatFlatSpec<Hocon>.() -> Unit) :
    SerialFormatFlatSpec<Hocon>(hocon, body)
