package cn.chengzhimeow.mhdfstartmenu.config

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager
import cn.chengzhimeow.mhdfstartmenu.action.Action
import cn.chengzhimeow.mhdfstartmenu.action.check
import cn.chengzhimeow.mhdfstartmenu.condition.check
import cn.chengzhimeow.mhdfstartmenu.util.config.getActionList
import cn.chengzhimeow.mhdfstartmenu.util.message.color
import cn.chengzhimeow.mhdfstartmenu.util.message.log

class ConfigSetting private constructor() : AbstractYamlManager(
    ConfigManager.instance.yamlManager
) {
    companion object {
        val instance by lazy { ConfigSetting() }
    }

    override fun originFilePath() = "config_zh.yml"
    override fun filePath() = "config.yml"

    val actions = mutableMapOf<String, List<Action>>()

    override fun reload() {
        super.reload()

        fun List<Action>.checkActions(key: String) {
            this.forEachIndexed { i, action ->
                action.actions.forEachIndexed { ai, a ->
                    val errors = a.check()
                    if (errors.isEmpty()) return@forEachIndexed
                    "&c无法解析配置下的 $key.$i.actions.$ai".color().log()
                    errors.forEach { e -> e.color().log() }
                }

                action.conditions.forEachIndexed { bi, b ->
                    val errors = b.check()
                    if (errors.isEmpty()) return@forEachIndexed
                    "&c无法解析配置下的 $key.$i.conditions.$bi".color().log()
                    errors.forEach { e -> e.color().log() }
                }
            }
        }

        val holograms = this.data.getConfigurationSection("hologram")!!
        holograms.keys.forEach { key ->
            val hologram = holograms.getConfigurationSection(key)!!
            actions[key] = hologram.getActionList("action").also {
                it.checkActions("hologram.$key.action")
            }
        }
    }
}