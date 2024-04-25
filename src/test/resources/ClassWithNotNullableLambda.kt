class ClassWithNotNullableLambda {

    fun methodeWithNotNullableLambda(notNullableLambda: (() -> Unit)) {
        notNullableLambda.invoke()
    }
}
