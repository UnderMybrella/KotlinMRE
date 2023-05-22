import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.core.spec.style.scopes.FunSpecRootScope
import io.kotest.mpp.env

inline fun FunSpecRootScope.contextEnv(key: String, default: () -> String, noinline block: suspend FunSpecContainerScope.() -> Unit) =
    context(env(key) ?: default(), block)