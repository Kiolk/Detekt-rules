[![](https://jitpack.io/v/Kiolk/Detekt-rules.svg)](https://jitpack.io/#Kiolk/Detekt-rules) ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.kiolk/kiolk-detekt-rules) ![GitHub Release](https://img.shields.io/github/v/release/kiolk/detekt-rules?color=yellow)

# kiolk-detekt-rules

---
Hello folks. This repository contains my own Detekt rules that born in result my work how developer. I am planning to add new rules in the future. You are welcome to contribute!


# How add to the project

---
You have different options how you can add this set of rules to your project. 

## Maven
To use Maven Central repository, you should add link on `mavenCentral()` in repositories block in `build.gradle.kt` file

```kotlin
repositories {
        mavenCentral()
    }
```
In `dependencies` block in `build.gradle.kt` of module where you will use detekt add reference on library that points on the latest version
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
In `dependencies` block in `build.gradle.kt` of module where you will use detekt add reference on library that points on the latest version
```kotlin
detektPlugins("com.github.Kiolk:Detekt-rules:v1.0.4")
```

## Local artifacts
If you want to use only locally, you should add path to `jar` file on your local machine. Latest artifacts you can find in [release section](https://github.com/Kiolk/Detekt-rules/releases) of repository.
```kotlin
detektPlugins(files("local_path_to_artifact.jar"))

```

# Configuration

---

# Rules:

---

## UseInvokeForOperator
### Motivation
### Cases
### AutoCorrection
