package cn.chengzhimeow.mhdfstartmenu.config

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager
import cn.chengzhimeow.mhdfstartmenu.Main
import cn.chengzhimeow.mhdfstartmenu.util.message.color
import cn.chengzhimeow.mhdfstartmenu.util.message.replace
import net.kyori.adventure.text.Component

class LangSetting private constructor() : AbstractYamlManager(
    ConfigManager.instance.yamlManager
) {
    companion object {
        val instance by lazy { LangSetting() }
    }

    override fun originFilePath() = "lang_zh.yml"
    override fun filePath() = "lang.yml"

    /**
     * 根据指定key获取语言文件对应文本并处理颜色
     *
     * @return 文本
     */
    fun i18n(key: String) = this.data.getString(key)
        ?.replace("{prefix}", this.data.getString("prefix")!!)
        ?.replace("{version}", Main.instance.description.version)
        ?.color() ?: throw NullPointerException()

    /**
     * 获取指定key下的项列表
     *
     * @return 项列表
     */
    fun getKeys(key: String) =
        super.getData().getConfigurationSection(key)?.getKeys(false) ?: emptySet()

    /**
     * 获取指定命令key命令信息文本实例
     *
     * @param command 命令key
     * @return 文本实例
     */
    fun getCommandInfo(command: String) = this.i18n("command_info_format")
        .replace("{usage}", this.i18n("$command.usage"))
        .replace("{description}", this.i18n("$command.description"))

    /**
     * 获取命令帮助
     *
     * @param prefix      前缀
     * @param commandList 命令列表
     * @return 命令帮助文本实例
     */
    fun getHelpList(
        prefix: String,
        commandList: Collection<String>
    ): Component {
        val builder = Component.empty().toBuilder()

        val endIndex = commandList.size - 1
        commandList.forEachIndexed { i, command ->
            builder.append(this.getCommandInfo("$prefix.$command"))
            if (i != endIndex) builder.appendNewline()
        }

        return builder.build()
    }

    /**
     * 获取命令帮助
     *
     * @param prefix 前缀
     * @return 命令帮助文本实例
     */
    fun getHelpList(prefix: String) = this.getHelpList(prefix, this.getKeys(prefix))
}