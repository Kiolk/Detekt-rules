class ClassWithNotNullableLambdaButWithSafeCall {

    fun methodeWithNotNullableLambdaButWithSaveCall(notNullableLambda: (() -> Unit)) {
        notNullableLambda?.invoke()
    }
}
