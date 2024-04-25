class ClassWithNotNullableLambdaWithSeveralArguments {

    fun methodeWithNotNullableLambda(callback: (Int, String) -> Unit) {
        callback.invoke(3, "string")
    }
}
