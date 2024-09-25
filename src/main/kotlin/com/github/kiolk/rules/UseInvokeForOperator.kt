package com.github.kiolk.rules

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.api.internal.RequiresTypeResolution
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.astReplace
import org.jetbrains.kotlin.resolve.calls.util.getType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.isNullable

@RequiresTypeResolution
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

        if (expression.isHasInvoke()) {
            val receivedType = expression.receiverExpression.getType(bindingContext)

            if (!isHasOperatorInvoke(receivedType)) return

            report(CodeSmell(issue, Entity.from(expression), ISSUE_DESCRIPTION))

            val invokeExpression = (expression.selectorExpression as? KtCallExpression) ?: return

            withAutoCorrect {
                expression.astReplace(
                    KtPsiFactory.contextual(expression)
                        .createExpression(
                            "${expression.firstChild.text}(${
                                invokeExpression.valueArguments.joinToString(
                                    ", "
                                ) { it.text }
                            })"
                        )
                )
            }
        }
    }

    override fun visitSafeQualifiedExpression(expression: KtSafeQualifiedExpression) {
        super.visitSafeQualifiedExpression(expression)

        if (expression.isHasInvoke() && isNotNullable(expression.receiverExpression)) {
            val receivedType = expression.receiverExpression.getType(bindingContext)

            if (!isHasOperatorInvoke(receivedType)) return

            report(CodeSmell(issue, Entity.from(expression), ISSUE_DESCRIPTION))

            val invokeExpression = (expression.selectorExpression as? KtCallExpression) ?: return

            withAutoCorrect {
                expression.astReplace(
                    KtPsiFactory.contextual(expression)
                        .createExpression(
                            "${expression.firstChild.text}(${
                                invokeExpression.valueArguments.joinToString(
                                    ", "
                                ) { it.text }
                            })"
                        )
                )
            }
        }
    }

    private fun KtQualifiedExpression.isHasInvoke(): Boolean {
        val calledExpression = this.selectorExpression?.firstChild ?: return false

        return calledExpression.text.contains(Regex("\\binvoke\\b"))
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

        return receiverType.memberScope.getContributedFunctions(
            Name.identifier("invoke"),
            NoLookupLocation.FROM_BACKEND
        ).firstOrNull { true }?.isOperator ?: false
    }

    companion object {
        const val ISSUE_DESCRIPTION =
            "This rule reports that you should use operator instead using directly call invoke"
    }
}
