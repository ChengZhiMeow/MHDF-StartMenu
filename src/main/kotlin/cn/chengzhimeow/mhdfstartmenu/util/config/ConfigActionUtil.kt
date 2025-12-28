package cn.chengzhimeow.mhdfstartmenu.util.config

import cn.chengzhimeow.ccaction.action.ActionBuilder
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection
import cn.chengzhimeow.mhdfstartmenu.action.Action
import cn.chengzhimeow.mhdfstartmenu.action.ActionManager

private fun ConfigurationSection.buildAction(): ActionBuilder.Builder {
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
                    v.buildAction()
                }.toList()
                return@forEach
            }
        }

        actionParms[k] = value
    }

    return ActionManager.instance.getAction(type, actionParms)
}

fun ConfigurationSection.getActionBuilderList(key: String) =
    this.getConfigurationSectionList(key)
        .map { it.buildAction() }
        .toList()

fun ConfigurationSection.getActionList(key: String) = this.getConfigurationSectionList(key).map {
    Action(
        it.getConditionBuilderList("conditions"),
        it.getActionBuilderList("actions")
    )
}.toList()