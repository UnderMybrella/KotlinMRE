import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.isActive

class Sample : FunSpec({
    test("isActive") {
        isActive shouldBe true
    }

    test("isInactive") {
        isActive shouldBe false
    }
})