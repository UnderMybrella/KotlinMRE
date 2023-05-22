import io.kotest.core.config.ProjectConfiguration
import io.kotest.core.test.NestedTest
import io.kotest.core.test.TestCase
import io.kotest.engine.spec.Materializer

class FlatSpecNesting(val configuration: ProjectConfiguration) {
    private val tests: MutableList<TestCase> = mutableListOf()
    private var readIndex: Int = 0
    private var writeIndex: Int = 0

    public fun addTest(test: NestedTest, parent: TestCase) {
        tests.add(writeIndex++, Materializer(configuration).materialize(test, parent))
    }

    public fun getTest(): TestCase? =
        tests.getOrNull(readIndex++)
}