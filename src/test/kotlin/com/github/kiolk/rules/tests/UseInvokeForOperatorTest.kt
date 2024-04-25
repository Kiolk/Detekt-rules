package com.github.kiolk.rules.tests

import com.github.kiolk.rules.UseInvokeForOperator
import io.github.detekt.test.utils.compileForTest
import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

@KotlinCoreEnvironmentTest
class UseInvokeForOperatorTest(private val environment: KotlinCoreEnvironment) {

    @Test
    fun `When lambda with invoke Then rule is thrown`() {
        val path = compileForTest(Path("src/test/resources/ClassWithNotNullableLambda.kt"))

        val findings: List<Finding> = UseInvokeForOperator(TestConfig("autoCorrect" to false)).compileAndLintWithContext(environment, path.text)

        assertEquals(1, findings.size)
        assertEquals(UseInvokeForOperator.ISSUE_DESCRIPTION, findings[0].issue.description)
    }

    @Test
    fun `When nullable lambda with invoke Then rule is not thrown`() {
        val path = compileForTest(Path("src/test/resources/ClassWithNullableLambda.kt"))

        val findings: List<Finding> = UseInvokeForOperator(TestConfig()).compileAndLintWithContext(environment, path.text)

        assertTrue(findings.isEmpty())
    }

    @Test
    fun `When lambda with invoke and call safe Then rule is thrown`() {
        val path = compileForTest(Path("src/test/resources/ClassWithNotNullableLambdaButWithSafeCall.kt"))

        val findings: List<Finding> = UseInvokeForOperator(TestConfig()).compileAndLintWithContext(environment, path.text)

        assertEquals(1, findings.size)
        assertEquals(UseInvokeForOperator.ISSUE_DESCRIPTION, findings[0].issue.description)
    }

    @Test
    fun `When lambda with invoke with several arguments Then rule is thrown`() {
        val path = compileForTest(Path("src/test/resources/ClassWithNotNullableLambdaWithSeveralArguments.kt"))

        val findings: List<Finding> = UseInvokeForOperator(TestConfig()).compileAndLintWithContext(environment, path.text)

        assertEquals(1, findings.size)
        assertEquals(UseInvokeForOperator.ISSUE_DESCRIPTION, findings[0].issue.description)
    }

    @Test
    fun `When class has function with name invoke Than rule is not thrown`() {
        val path = compileForTest(Path("src/test/resources/ClassWithMethodeInvoke.kt"))

        val findings: List<Finding> = UseInvokeForOperator(TestConfig()).compileAndLintWithContext(environment, path.text)

        assertTrue(findings.isEmpty())
    }

    @Test
    fun `When class has operator with name invoke Than rule is thrown`() {
        val path = compileForTest(Path("src/test/resources/ClassWithOperatorInvoke.kt"))

        val findings: List<Finding> = UseInvokeForOperator(TestConfig()).compileAndLintWithContext(environment, path.text)

        assertEquals(1, findings.size)
        assertEquals(UseInvokeForOperator.ISSUE_DESCRIPTION, findings[0].issue.description)
    }
}
