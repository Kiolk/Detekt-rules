package com.github.kiolk.rules.utils

import io.github.detekt.test.utils.compileContentForTest
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.psi.KtFile
import kotlin.test.assertEquals

abstract class AutoCorrectTest<T : Rule>(factory: (config: Config) -> T, additionalConfig: List<Pair<String, Any>> = listOf()) {

    private val rule: T = factory(TestConfig("autoCorrect" to false, *additionalConfig.toTypedArray()))
    private val autoCorrectRule: T = factory(TestConfig("autoCorrect" to true, *additionalConfig.toTypedArray()))

    fun assertLintAndFormat(
        @Language("kotlin")
        codeToLint: String,
        @Language("kotlin")
        codeAfterCorrection: String,
        issueDescription: String,
    ) {
        val fileName = "Test.kt"
        val code = compileContentForTest(codeToLint, fileName)
        val findings = autoCorrectRule.lint(code)

        assertThat(findings)
            .`as`("Expected rule to be: '${issueDescription}'")
            .extracting<String> { it.message }
            .allMatch { it == issueDescription }
        assertEquals(codeAfterCorrection, code.text)
    }

    fun assertLintAndFormat(
        codeToLint: KtFile,
        codeAfterCorrection: KtFile,
        issueDescription: String,
    ) {
        assertLintAndFormat(codeToLint.text, codeAfterCorrection.text, issueDescription)
    }
}
