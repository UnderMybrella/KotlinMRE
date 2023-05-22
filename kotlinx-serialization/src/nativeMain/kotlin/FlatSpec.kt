import io.kotest.core.spec.KotestTestScope
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.core.spec.style.scopes.FunSpecRootScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

public actual typealias FlatSpecRootScope = FunSpecRootScope

@KotestTestScope
public actual typealias FlatSpecContainerScope = FunSpecContainerScope

public actual abstract class FlatSpec actual constructor(body: FlatSpec.() -> Unit): FunSpec(), FlatSpecRootScope {
    init {
        body()
    }
}


public actual fun <T> FlatSpecRootScope.context(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
) = context(name) { test(CompletableDeferred(supplier())) }

public actual fun <T> FlatSpecRootScope.xcontext(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
) = xcontext(name) { test(CompletableDeferred(supplier())) }

public actual suspend fun <T> FlatSpecContainerScope.context(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
) = context(name) { test(CompletableDeferred(supplier())) }

public actual suspend fun <T> FlatSpecContainerScope.xcontext(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
) = xcontext(name) { test(CompletableDeferred(supplier())) }