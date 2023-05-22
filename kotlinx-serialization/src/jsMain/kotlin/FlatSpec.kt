import io.kotest.core.names.TestName
import io.kotest.core.spec.DslDrivenSpec
import io.kotest.core.spec.KotestTestScope
import io.kotest.core.spec.RootTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.scopes.AbstractContainerScope
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.core.spec.style.scopes.RootScope
import io.kotest.core.spec.style.scopes.addTest
import io.kotest.core.test.TestScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private const val FLAT_SPEC_DEPRECATED_MESSAGE = "Single scope nesting is impossible in JS at this time due to issues with promises and existing runners (see https://github.com/kotest/kotest/issues/3141)"
private const val FLAT_SPEC_DEPRECATED_REPLACEMENT = "context(name, { Unit }, test)"

public actual interface FlatSpecRootScope : RootScope, CoroutineScope {
    // Deprecate and hide in JS, but if we're using an actual declaration then we should add
    @Deprecated(FLAT_SPEC_DEPRECATED_MESSAGE, ReplaceWith(FLAT_SPEC_DEPRECATED_REPLACEMENT), DeprecationLevel.ERROR)
    actual fun context(name: String, test: suspend FlatSpecContainerScope.() -> Unit) =
        addTest(TestName("context ", name, false), false, null) { test(FlatSpecContainerScope(this)) }

    @Deprecated(FLAT_SPEC_DEPRECATED_MESSAGE, ReplaceWith(FLAT_SPEC_DEPRECATED_REPLACEMENT), DeprecationLevel.ERROR)
    actual fun xcontext(name: String, test: suspend FlatSpecContainerScope.() -> Unit) =
        addTest(TestName("context ", name, false), true, null) { test(FlatSpecContainerScope(this)) }

    actual fun test(name: String, test: suspend TestScope.() -> Unit) =
        addTest(TestName(name), false, null, test)

    actual fun xtest(name: String, test: suspend TestScope.() -> Unit) =
        addTest(TestName(name), true, null, test)
}

@KotestTestScope
public actual class FlatSpecContainerScope(internal val testName: TestName, internal val scope: FlatSpecRootScope) {
    constructor(scope: TestScope): this(scope.testCase.name, scope.testCase.spec as FlatSpecRootScope)

    @Deprecated(FLAT_SPEC_DEPRECATED_MESSAGE, ReplaceWith(FLAT_SPEC_DEPRECATED_REPLACEMENT), DeprecationLevel.ERROR)
    actual suspend fun context(name: String, test: suspend FlatSpecContainerScope.() -> Unit) =
        scope.addTest(testName.subtest(name), false, null) { test(FlatSpecContainerScope(this)) }

    @Deprecated(FLAT_SPEC_DEPRECATED_MESSAGE, ReplaceWith(FLAT_SPEC_DEPRECATED_REPLACEMENT), DeprecationLevel.ERROR)
    actual suspend fun xcontext(name: String, test: suspend FlatSpecContainerScope.() -> Unit) =
        scope.addTest(testName.subtest(name), true, null) { test(FlatSpecContainerScope(this)) }

    actual suspend fun test(name: String, test: suspend TestScope.() -> Unit) =
        scope.addTest(testName.subtest(name), false, null, test)

    actual suspend fun xtest(name: String, test: suspend TestScope.() -> Unit) =
        scope.addTest(testName.subtest(name), true, null, test)
}

public actual abstract class FlatSpec actual constructor(body: FlatSpec.() -> Unit) : FunSpec(), FlatSpecRootScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

    override fun context(name: String, test: suspend FunSpecContainerScope.() -> Unit) =
        addTest(TestName("context ", name, false), false, null) { test(FunSpecContainerScope(this)) }

    override fun xcontext(name: String, test: suspend FunSpecContainerScope.() -> Unit) =
        addTest(TestName("context ", name, false), false, null) { test(FunSpecContainerScope(this)) }

    actual override fun test(name: String, test: suspend TestScope.() -> Unit) =
        super<FlatSpecRootScope>.test(name, test)

    actual override fun xtest(name: String, test: suspend TestScope.() -> Unit) =
        super<FlatSpecRootScope>.xtest(name, test)

    init {
        body()
    }
}

internal fun TestName.subtest(subtest: String): TestName =
    testName.run { copy(testName = "$testName / $subtest") }

public actual fun <T> FlatSpecRootScope.context(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
) {
    val testName = TestName("context ", name, false)
    val scope = FlatSpecContainerScope(testName, this)
    val data = CompletableDeferred<T>(coroutineContext[Job])

    addTest(testName, false, null) {
        data.complete(supplier(scope))
    }

    launch { test(scope, data) }
}

public actual fun <T> FlatSpecRootScope.xcontext(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
) {
    val testName = TestName("context ", name, false)
    val scope = FlatSpecContainerScope(testName, this)
    val data = CompletableDeferred<T>(coroutineContext[Job])

    addTest(testName, true, null) {
        data.complete(supplier(scope))
    }

    // we don't launch the test scope here because it never runs
//    launch { test(scope, data) }
}

public actual suspend fun <T> FlatSpecContainerScope.context(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
) {
    val testName = testName.subtest(name)
    val scope = FlatSpecContainerScope(testName, scope)
    val data = CompletableDeferred<T>(this.scope.coroutineContext[Job])

    this.scope.addTest(testName, false, null) {
        data.complete(supplier(scope))
    }

    test(scope, data)
}

public actual suspend fun <T> FlatSpecContainerScope.xcontext(
    name: String,
    supplier: suspend FlatSpecContainerScope.() -> T,
    test: suspend FlatSpecContainerScope.(Deferred<T>) -> Unit,
)  {
    val testName = testName.subtest(name)
    val scope = FlatSpecContainerScope(testName, scope)
    val data = CompletableDeferred<T>(this.scope.coroutineContext[Job])

    this.scope.addTest(testName, false, null) {
        data.complete(supplier(scope))
    }

    // we don't launch the test scope here because it never runs
    test(scope, data)
}