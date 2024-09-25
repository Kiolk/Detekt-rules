class ClassWithMethodeInvoke {

    fun invokeMethode() {}
}

class SomeClass {

    fun someFunction() {
        val classWithMethodeInvoke = ClassWithMethodeInvoke()
        classWithMethodeInvoke.invokeMethode()
    }
}
