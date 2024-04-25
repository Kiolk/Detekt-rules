class ClassWithOperatorInvoke {

    operator fun invoke() {}
}

class SomeClassExecutor {

    fun someFunction() {
        val classWithOperatorInvoke = ClassWithOperatorInvoke()
        classWithOperatorInvoke.invoke()
    }
}
