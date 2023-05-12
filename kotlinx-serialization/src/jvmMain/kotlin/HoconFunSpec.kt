import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon

@OptIn(ExperimentalSerializationApi::class)
abstract class HoconFunSpec(hocon: Hocon = Hocon, body: SerialFormatFunSpec<Hocon>.() -> Unit) :
    SerialFormatFunSpec<Hocon>(hocon, body)
