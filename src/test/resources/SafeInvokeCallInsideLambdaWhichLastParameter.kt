inline fun reportException(
    throwable: Throwable,
    message: String? = null,
    noinline customKeys: (KeyValueBuilder.() -> Unit),
) {
    Timber.e(throwable, message)
    if (throwable is CancellationException) throw throwable
    Firebase?.crashlytics?.recordException(throwable) {
        customKeys?.invoke()
        if (message != null) key("message", message)
    }
}