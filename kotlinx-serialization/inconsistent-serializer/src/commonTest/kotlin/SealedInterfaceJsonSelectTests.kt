import io.kotest.assertions.throwables.shouldThrowAnyUnit
import io.kotest.engine.test.names.appendTagsInDisplayName
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.mpp.env
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
class SealedInterfaceJsonSelectTests : JsonFlatSpec(body = {
    println("Loading in!")
    val encoded = Json.encodeToString(SealedInterfaceNamedCompanion.StringType("StringType", 0, "Hello, World!"))

    assertSoftly = true

    context("SealedInterfaceJsonSelect", {}) {
        test("reified") {
            val reified = shouldSerialise { serializersModule.serializer<SealedInterfaceNamedCompanion>() }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(reified, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceNamedCompanion.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeOf") {
            val explicit = shouldSerialise { serializersModule.serializer(typeOf<SealedInterfaceNamedCompanion>()) }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceNamedCompanion.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeInfoOf") {
            val explicit =
                shouldSerialise { serializersModule.serializerForTypeInfo(typeInfo<SealedInterfaceNamedCompanion>()) }
                    .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceNamedCompanion.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("Companion.serializer()") {
            val explicit = SealedInterfaceNamedCompanion.serializer()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceNamedCompanion.StringType>()
                .data shouldBe "Hello, World!"
        }
    }

    context("SealedInterfaceExplicitObject", {}) {
        test("reified") {
            val reified = shouldSerialise { serializersModule.serializer<SealedInterfaceExplicitObject>() }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(reified, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeOf") {
            val explicit = shouldSerialise { serializersModule.serializer(typeOf<SealedInterfaceExplicitObject>()) }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeInfo") {
            val explicit =
                shouldSerialise { serializersModule.serializerForTypeInfo(typeInfo<SealedInterfaceExplicitObject>()) }
                    .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("Companion.serializer()") {
            val explicit = SealedInterfaceExplicitObject.serializer()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SealedInterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("List<>") {
            val explicit =
                shouldSerialise { serializersModule.serializer(typeOf<List<SealedInterfaceExplicitObject>>()) }
                    .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, "[${encoded}]") }
                .shouldNotBeNull()
                .shouldBeInstanceOf<List<*>>()
                .first()
                .shouldBeTypeOf<SealedInterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeInfo List<>") {
            val explicit =
                shouldSerialise { serializersModule.serializerForTypeInfo(typeInfo<List<SealedInterfaceExplicitObject>>()) }
                    .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, "[${encoded}]") }
                .shouldNotBeNull()
                .shouldBeInstanceOf<List<*>>()
                .first()
                .shouldBeTypeOf<SealedInterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }
    }

    context("InterfaceExplicitObject", {}) {
        test("reified") {
            val reified = shouldSerialise { serializersModule.serializer<InterfaceExplicitObject>() }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(reified, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<InterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeOf") {
            val explicit = shouldSerialise { serializersModule.serializer(typeOf<InterfaceExplicitObject>()) }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<InterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeInfo") {
            val explicit =
                shouldSerialise { serializersModule.serializerForTypeInfo(typeInfo<InterfaceExplicitObject>()) }
                    .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, encoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<InterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("List<>") {
            val explicit = shouldSerialise { serializersModule.serializer(typeOf<List<InterfaceExplicitObject>>()) }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, "[${encoded}]") }
                .shouldNotBeNull()
                .shouldBeInstanceOf<List<*>>()
                .first()
                .shouldBeTypeOf<InterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }

        test("typeInfo List<>") {
            val explicit =
                shouldSerialise { serializersModule.serializerForTypeInfo(typeInfo<List<InterfaceExplicitObject>>()) }
                    .shouldNotBeNull()

            shouldSerialise { decodeFromString(explicit, "[${encoded}]") }
                .shouldNotBeNull()
                .shouldBeInstanceOf<List<*>>()
                .first()
                .shouldBeTypeOf<InterfaceExplicitObject.StringType>()
                .data shouldBe "Hello, World!"
        }
    }

    context("SimpleNamedCompanion", {}) {
        test("reified") {
            val simple = shouldSerialise { serializersModule.serializer<SimpleNamedCompanion>() }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(simple, "\"Hello, World!\"") }
                .shouldNotBeNull()
                .shouldBeTypeOf<SimpleNamedCompanion>()
                .string shouldBe "Hello, World!"
        }

        test("explicit") {
            val simple = shouldSerialise { serializersModule.serializer(typeOf<SimpleNamedCompanion>()) }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(simple, "\"Hello, World!\"") }
                .shouldNotBeNull()
                .shouldBeTypeOf<SimpleNamedCompanion>()
                .string shouldBe "Hello, World!"
        }
    }

    context("SimpleNested", {}) {
        test("explicit") {
            val nested = shouldSerialise { serializersModule.serializer(typeOf<SimpleNested>()) }
                .shouldNotBeNull()

            val nestedEncoded = shouldSerialise {
                encodeToString(
                    nested,
                    SimpleNested(decodeFromString(encoded), decodeFromString("\"Hello, World!\""))
                )
            }
                .shouldNotBeNull()

            shouldSerialise { decodeFromString(nested, nestedEncoded) }
                .shouldNotBeNull()
                .shouldBeTypeOf<SimpleNested>()
        }
    }

    test("List<Any> serialise") {
        val list = shouldSerialise { serializersModule.serializerForTypeInfo(typeInfo<List<Any>>()) }
            .shouldNotBeNull()
            .shouldBeInstanceOf<KSerializer<List<Any>>>()

        shouldSerialise { encodeToString(list, emptyList()) }
    }

    test("List<Any> deserialise") {
        val list = shouldSerialise { serializersModule.serializerForTypeInfo(typeInfo<List<Any>>()) }
            .shouldNotBeNull()
            .shouldBeInstanceOf<KSerializer<List<Any>>>()

        shouldSerialise { decodeFromString(list, "[]") }
    }

    test("Simple Encode Test") {
        Json.encodeToString(
            Json.serializersModule.serializerForTypeInfo(typeInfo<List<Any>>()) as KSerializer<List<Any>>,
            emptyList()
        ) shouldBe "[]"
    }

    test("Simple Decode Test") {
        Json.decodeFromString(
            Json.serializersModule.serializerForTypeInfo(typeInfo<List<Any>>()) as KSerializer<List<Any>>,
            "[]"
        ).shouldBeInstanceOf<JsonArray>() shouldHaveSize 0
    }
})