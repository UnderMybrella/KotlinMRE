import io.kotest.core.names.TestName
import io.kotest.core.spec.DslDrivenSpec
import io.kotest.core.spec.KotestTestScope
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.scopes.RootScope
import io.kotest.core.spec.style.scopes.addTest
import io.kotest.core.test.TestScope
import kotlinx.coroutines.Deferred

public expect interface FlatSpecRootScope : RootScope {
    open fun context(name: String, test: suspend FlatSpecContainerScope.() -> Unit)
    open fun xcontext(name: String, test: suspend FlatSpecContainerScope.() -> Unit)

    open fun test(name: String, test: suspend TestScope.() -> Unit)
    open fun xtest(name: String, test: suspend TestScope.() -> Unit)
}

@KotestTestScope
public expect class FlatSpecContainerScope {
    suspend fun context(name: String, test: suspend FlatSpecContainerScope.() -> Unit)
    suspend fun xcontext(name: String, test: suspend FlatSpecContainerScope.() -> Unit)

    suspend fun test(name: String, test: suspend TestScope.() -> Unit)
    suspend fun xtest(name: String, test: suspend TestScope.() -> Unit)
}

public expect abstract class FlatSpec(body: FlatSpec.() -> Unit) : FunSpec, FlatSpecRootScope {
    override fun test(name: String, test: suspend TestScope.() -> Unit)
    override fun xtest(name: String, test: suspend TestScope.() -> Unit)

}

public expect fun <T> FlatSpecRootScope.context(name: String, supplier: suspend FlatSpecContainerScope.() -> T, test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit)
public expect fun <T> FlatSpecRootScope.xcontext(name: String, supplier: suspend FlatSpecContainerScope.() -> T, test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit)

public expect suspend fun <T> FlatSpecContainerScope.context(name: String, supplier: suspend FlatSpecContainerScope.() -> T, test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit)
public expect suspend fun <T> FlatSpecContainerScope.xcontext(name: String, supplier: suspend FlatSpecContainerScope.() -> T, test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit)
