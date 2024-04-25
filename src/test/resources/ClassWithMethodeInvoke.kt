class ClassWithMethodeInvoke {

    fun invoke() {}
}

class SomeClass {

    fun someFunction() {
        val classWithMethodeInvoke = ClassWithMethodeInvoke()
        classWithMethodeInvoke.invoke()
    }
}
