package com.github.kiolk.rules.tests

import com.github.kiolk.rules.UseInvokeForOperator
import com.github.kiolk.rules.utils.AutoCorrectTest
import org.junit.jupiter.api.Test

class UseInvokeForOperatorAutoCorrectTest :
    AutoCorrectTest<UseInvokeForOperator>(factory = { config -> UseInvokeForOperator(config) }, additionalConfig = listOf(Pair("isTest", true))) {

    @Test
    fun `When class with operator invoke Then autoCorrect to direct call`() {
        assertLintAndFormat(
            codeToLint = """
            class ClassWithOperatorInvoke {

                operator fun invoke() {}
            }

            class SomeClassExecutor {

                fun someFunction() {
                    val classWithOperatorInvoke = ClassWithOperatorInvoke()
                    classWithOperatorInvoke.invoke()
                }
            }
        """.trimIndent(),
            codeAfterCorrection = """
            class ClassWithOperatorInvoke {

                operator fun invoke() {}
            }

            class SomeClassExecutor {

                fun someFunction() {
                    val classWithOperatorInvoke = ClassWithOperatorInvoke()
                    classWithOperatorInvoke()
                }
            }
            """.trimIndent(),
            issueDescription = UseInvokeForOperator.ISSUE_DESCRIPTION
        )
    }

    @Test
    fun `When lambda use invoke Then autoCorrect to direct call`() {
        assertLintAndFormat(
            codeToLint = """
                class ClassWithNotNullableLambda {

                    fun methodeWithNotNullableLambda(notNullableLambda: (() -> Unit)) {
                        notNullableLambda.invoke()
                    }
                }
            """.trimIndent(),
            codeAfterCorrection = """
                class ClassWithNotNullableLambda {

                    fun methodeWithNotNullableLambda(notNullableLambda: (() -> Unit)) {
                        notNullableLambda()
                    }
                }
            """.trimIndent(),
            issueDescription = UseInvokeForOperator.ISSUE_DESCRIPTION
        )
    }

    @Test
    fun `When lambda use invoke and arguments Then autoCorrect to direct call`() {
        assertLintAndFormat(
            codeToLint = """
                class ClassWithNotNullableLambdaWithSeveralArguments {

                    fun methodeWithNotNullableLambda(callback: (Int, String) -> Unit) {
                        callback.invoke(3, "string")
                    }
                }
            """.trimIndent(),
            codeAfterCorrection = """
                class ClassWithNotNullableLambdaWithSeveralArguments {

                    fun methodeWithNotNullableLambda(callback: (Int, String) -> Unit) {
                        callback(3, "string")
                    }
                }
            """.trimIndent(),
            issueDescription = UseInvokeForOperator.ISSUE_DESCRIPTION
        )
    }

    @Test
    fun `When lambda with unnecessary save call Then autoCorrect to direct call`() {
        assertLintAndFormat(
            codeToLint = """
                class ClassWithNotNullableLambdaWithSeveralArguments {

                    fun methodeWithNotNullableLambda(callback: (Int, String, Float) -> Unit) {
                        callback?.invoke(3, "string", 3.0)
                    }
                }
            """.trimIndent(),
            codeAfterCorrection = """
                class ClassWithNotNullableLambdaWithSeveralArguments {

                    fun methodeWithNotNullableLambda(callback: (Int, String, Float) -> Unit) {
                        callback(3, "string", 3.0)
                    }
                }
            """.trimIndent(),
            issueDescription = UseInvokeForOperator.ISSUE_DESCRIPTION
        )
    }
}
