[![](https://jitpack.io/v/Kiolk/Detekt-rules.svg)](https://jitpack.io/#Kiolk/Detekt-rules) ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.kiolk/kiolk-detekt-rules) ![GitHub Release](https://img.shields.io/github/v/release/kiolk/detekt-rules?color=yellow) ![Create Github Release](https://github.com/Kiolk/Detekt-rules/actions/workflows/create_github_release.yml/badge.svg) ![Publish to Maven Central Repository](https://github.com/Kiolk/Detekt-rules/actions/workflows/publish_to_maven_central.yml/badge.svg)
# kiolk-detekt-rules
Hello folks. This repository contains my own Detekt rules that born in result my work how developer. I am planning to
add new rules in the future. You are welcome to contribute!

# How add to the project
You have different options how you can add this set of rules to your project.

## Maven
To use Maven Central repository, you should add link on `mavenCentral()` in repositories block in `build.gradle.kt` file

```kotlin
repositories {
    mavenCentral()
}
```

In `dependencies` block in `build.gradle.kt` of module where you will use detekt add reference on library that points on
the latest version

```kotlin
detektPlugins("io.github.kiolk:kiolk-detekt-rules:1.0.4")
```

## Jitpack
To use Jitpack, you should add link on jitpack in repositories block in `build.gradle.kt` file

```kotlin
repositories {
    maven { url 'https://jitpack.io' }
}
```

In `dependencies` block in `build.gradle.kt` of module where you will use detekt add reference on library that points on
the latest version

```kotlin
detektPlugins("com.github.Kiolk:Detekt-rules:v1.0.4")
```

## Local artifacts
If you want to use only locally, you should add path to `jar` file on your local machine. Latest artifacts you can find
in [release section](https://github.com/Kiolk/Detekt-rules/releases) of repository.

```kotlin
detektPlugins(files("local_path_to_artifact.jar"))

```

# Configuration
You can add this configuration of rule set to root `detekt.yml` file for more custom configuration.

```yaml
kiolk-detekt-rules:
  active: true
  UseInvokeForOperator:
    active: true
    autoCorrect: true #is false by default

```

# Rules:
## UseInvokeForOperator
### Motivation
If the class defines a function with name invoke and with keyword operator, it can execute this function by directly
call with round brackets. This simplification makes code more readable. This rule detekt such cases and can replace it
on direct call. It is applicable and for lambda functions.

### Cases
When class defines operator invoke
```kotlin
fun someFunction() {
    val classWithMethodeInvoke = ClassWithMethodeInvoke()
    classWithMethodeInvoke.invoke()
}
```
When lambda expression execute
```kotlin
fun methodeWithNotNullableLambda(notNullableLambda: (() -> Unit)) {
    notNullableLambda.invoke()
}
```

It does not trigger for nullable variable, because is not possible to call invoke without safety check

```kotlin
 fun methodeWithNullableLambda(nullableLambda: (() -> Unit)?) {
    nullableLambda?.invoke()
}
```
But triggers, if safety check is not necessary
```kotlin
fun methodeWithNotNullableLambdaButWithSaveCall(notNullableLambda: (() -> Unit)) {
    notNullableLambda?.invoke()
}
```

### AutoCorrection
Replaces `invoke` on direct call.

Before
```kotlin
fun methodeWithNotNullableLambda(callback: (Int, String) -> Unit) {
        callback.invoke(3, "string")
    }
```
After
```kotlin
fun methodeWithNotNullableLambda(callback: (Int, String) -> Unit) {
        callback(3, "string")
    }
```
