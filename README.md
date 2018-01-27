 [ ![Download](https://api.bintray.com/packages/vorpal-research/kotlin-maven/ktuples/images/download.svg) ](https://bintray.com/vorpal-research/kotlin-maven/ktuples/_latestVersion)
 [![Build Status](https://travis-ci.org/belyaev-mikhail/ktuples.svg?branch=master)](https://travis-ci.org/belyaev-mikhail/ktuples)
# ktuples
Tuple and variant types for kotlin

**Warning**: generated code inside!

Ever wondered why Kotlin have only two tuple types (`Pair` and `Triple`)? Because you usually don't need more.
If you **really** do, use this library. There are two basic concepts available -- Tuples and Variants

## Tuples

This lib provides you with types `Tuple0`, `Tuple1`, `Tuple2` and so on (up to `Tuple7` by default) 
of uniform heterogeneous collections of fixed sizes that works the way they are supposed to work.

There are some additional helper functions and utility constructs available, but the basics are as simple as that:
a number of standard kotlin data classes ready to use with any types you want.

Should you use these types to handle your specific cases? Nah. Just roll up your own data class. Kotlin is awesome at that.
These are mostly useful when you want to be as generic as possible or the name of the type you would make doesn't make any sense.

All the tuples in this library are immutable. We may add `MutableTuple$N` types for mutable usecases in the future.

## Variants

This lib provides you with types `EitherOf2`, `EitherOf3`, `EitherOf4` and so on (up to `EitherOf7` by default) 
of generic tagged unions that may contain one of several different types that are represented by types
`Variant0`, `Variant1`, `Variant2` (up to `Variant7` by default). Kotlin type system handles the rest 
(say, `Variant2(4)` is not a valid value of type `EitherOf2<Int, String>` or  `EitherOf3<Int, Double, String>`, but
it a valid value of type `EitherOf3<String, Double, Int>`). Values are always boxed, so beware.

As for tuples, when in doubt, roll up your own hierarchy of data classes: these are for generic use mostly.

Another nice Kotlin feature is that you get exhaustive `when` matching for free:

```kotlin
val x: EitherOf3<Int, Double, String> = ...
when(x) {
    is Variant0 -> functionOfInt(x.value) // x.value is Int
    is Variant1 -> functionOfDouble(x.value) // x.value is Double
    is Variant2 -> functionOfString(x.value) // x.value is String
    // you don't need an else here!~
}
```

## Misc

There are other experimental features in this library that wait to be documented.
