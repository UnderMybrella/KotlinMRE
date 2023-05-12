# Inconsistent Serializers

There's a few different problems here that I've found when
investigating [KTOR-5898](https://youtrack.jetbrains.com/issue/KTOR-5898).

## Bug: Declarations marked with `@Serializable` that have a named companion object fail in `serializer(type)` on the JVM

[Example](src/commonTest/kotlin/SealedInterfaceJsonSelectTests.kt)

Related issues:

- [kotlinx.serialization#1291](https://github.com/Kotlin/kotlinx.serialization/issues/1291)
- [kotlinx.serialization#1207](https://github.com/Kotlin/kotlinx.serialization/issues/1207)
- [kotlinx.serialization#2163](https://github.com/Kotlin/kotlinx.serialization/issues/2163)

This one is a little obscure, but boils down to the JVM implementation
for [kotlinx.serialization.internal.compiledSerializerImpl](https://github.com/Kotlin/kotlinx.serialization/blob/master/core/jvmMain/src/kotlinx/serialization/internal/Platform.kt#L21),
which in turn tries to retrieve the serializer from the companion
object
in [Platform#invokeSerializerOnCompanion](https://github.com/Kotlin/kotlinx.serialization/blob/master/core/jvmMain/src/kotlinx/serialization/internal/Platform.kt#L104).

On the JVM, however, this checks for a companion object by looking for a declared field named "Companion", which fails
if the companion object is named.

However, these issues do *not* occur when the serializer is called inline, whether that be through the use of an inline
reified function (such as `serializer<T>()` or `encodeToString<T>()`), or when nested in a class (
See [SimpleNested](src/commonMain/kotlin/SimpleNested.kt)).

## (Related) Warning: ktor-kotlinx-serialization - `serializerForTypeInfo` behaves in an unexpected way when given a bad complex type

I wasn't sure how to best name this, but this is the function that causes the core of the issue.

In `serializerOrNull`, null is returned if any of the type arguments for a type with them fails to retrieve a valid
serializer. This causes problems when `serializerForTypeInfo` is given a complex type, like `List<T>`.

What ends up happening is that ktor will end up trying to get a polymorphic serializer for the base List class, which
will fail, and give an unhelpful error message, such as:
- `kotlinx.serialization.SerializationException: Class 'ArrayList' is not registered for polymorphic serialization in the scope of 'List'.`
- `kotlinx.serialization.json.internal.JsonDecodingException: Expected class kotlinx.serialization.json.JsonObject as the serialized body of kotlinx.serialization.Polymorphic<List>, but had class kotlinx.serialization.json.JsonArray`

(Produced by calling `serializerForTypeInfo(typeInfo<List<KClass<*>>>())`)

While this is ultimately caused by the inner type failing to produce a valid serializer, the error message makes it incredibly difficult to figure out what is actually causing the problem.