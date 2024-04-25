package com.github.kiolk.provider

import com.github.kiolk.rules.UseInvokeForOperator
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class RuleProvider : RuleSetProvider {

    override val ruleSetId: String = "kiolk-detekt-rules"
    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            UseInvokeForOperator(config),
        )
    )
}
