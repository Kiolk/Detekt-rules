class ClassWithNullableLambda {

    fun methodeWithNullableLambda(nullableLambda: (() -> Unit)?) {
        nullableLambda?.invoke()
    }
}
