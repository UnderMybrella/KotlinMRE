import kotlinx.serialization.Serializable

@Serializable
data class SimpleNested(
    val sealed: SealedInterfaceNamedCompanion,
    val named: SimpleNamedCompanion
)
