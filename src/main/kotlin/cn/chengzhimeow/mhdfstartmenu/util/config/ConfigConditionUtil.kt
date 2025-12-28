package cn.chengzhimeow.mhdfstartmenu.util.config

import cn.chengzhimeow.cccondition.condition.ConditionBuilder
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection
import cn.chengzhimeow.mhdfstartmenu.condition.ConditionManager

private fun ConfigurationSection.buildCondition(): ConditionBuilder.Builder {
    val type = this.getString("type")!!

    val actionParms = mutableMapOf<String, Any?>()
    this.getKeys(true).forEach { k ->
        if (k == "type") return@forEach
        val value = this[k]

        run {
            if (value is List<*>) {
                if (value.isEmpty()) return@run
                if (value.first() !is ConfigurationSection) return@run

                actionParms[k] = value.map { v ->
                    v as ConfigurationSection
                    v.buildCondition()
                }.toList()
                return@forEach
            }
        }

        actionParms[k] = value
    }

    return ConditionManager.instance.getCondition(type, actionParms)
}

fun ConfigurationSection.getConditionBuilderList(key: String) =
    this.getConfigurationSectionList(key)
        .map { it.buildCondition() }
        .toList()