package com.github.kiolk.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.KtSafeQualifiedExpression
import org.jetbrains.kotlin.psi.psiUtil.astReplace
import org.jetbrains.kotlin.resolve.calls.util.getType
import org.jetbrains.kotlin.resolve.scopes.findFirstFunction
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.isNullable

//@RequiresTypeResolution
class UseInvokeForOperator(config: Config) : Rule(config) {

    override val issue: Issue
        get() = Issue(
            id = javaClass.simpleName,
            severity = Severity.CodeSmell,
            description = ISSUE_DESCRIPTION,
            debt = Debt.FIVE_MINS,
        )

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)

        if (expression.text.contains(Regex("invoke\\(.*\\)"))) {
            val receivedType = expression.receiverExpression.getType(bindingContext)

            if (!isHasOperatorInvoke(receivedType)) return

            report(CodeSmell(issue, Entity.from(expression), ISSUE_DESCRIPTION))

            val invokeExpression = (expression.selectorExpression as? KtCallExpression) ?: return

            withAutoCorrect {
                expression.astReplace(
                    KtPsiFactory.contextual(expression)
                        .createExpression("${expression.firstChild.text}(${invokeExpression.valueArguments.joinToString(", ") { it.text }})")
                )
            }
        }
    }

    override fun visitSafeQualifiedExpression(expression: KtSafeQualifiedExpression) {
        super.visitSafeQualifiedExpression(expression)

        if (expression.text.contains(Regex("invoke\\(.*\\)")) && isNotNullable(expression.receiverExpression)) {
            val receivedType = expression.receiverExpression.getType(bindingContext)

            if (!isHasOperatorInvoke(receivedType)) return

            report(CodeSmell(issue, Entity.from(expression), ISSUE_DESCRIPTION))

            val invokeExpression = (expression.selectorExpression as? KtCallExpression) ?: return

            withAutoCorrect {
                expression.astReplace(
                    KtPsiFactory.contextual(expression)
                        .createExpression("${expression.firstChild.text}(${invokeExpression.valueArguments.joinToString(", ") { it.text }})")
                )
            }
        }
    }

    private fun isNotNullable(receiverExpression: KtExpression): Boolean {
        if (this.ruleSetConfig.valueOrNull<Boolean>("isTest") == true) {
            return true
        }

        return receiverExpression.getType(bindingContext)?.isNullable() == false
    }

    private fun isHasOperatorInvoke(receiverType: KotlinType?): Boolean {
        if (this.ruleSetConfig.valueOrNull<Boolean>("isTest") == true) {
            return true
        }

        if (receiverType == null) {
            return false
        }

        return receiverType.memberScope.findFirstFunction("invoke") { true }.isOperator
    }

    companion object {
        const val ISSUE_DESCRIPTION = "This rule reports that you should use operator instead using directly call invoke"
    }
}
