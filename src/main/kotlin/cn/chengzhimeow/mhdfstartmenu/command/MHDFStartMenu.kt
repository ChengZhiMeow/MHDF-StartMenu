package cn.chengzhimeow.mhdfstartmenu.command

import cn.chengzhimeow.mhdfstartmenu.config.ConfigSetting
import cn.chengzhimeow.mhdfstartmenu.config.LangSetting
import cn.chengzhimeow.mhdfstartmenu.menu.joinMenu
import cn.chengzhimeow.mhdfstartmenu.menu.quitMenu
import cn.chengzhimeow.mhdfstartmenu.util.message.replace
import cn.chengzhimeow.mhdfstartmenu.util.message.sendMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class MHDFStartMenu : TabExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (args.isNotEmpty()) {
            when (args[0]) {
                "reload" -> {
                    ConfigSetting.instance.reload()
                    LangSetting.instance.reload()

                    LangSetting.instance.i18n("commands.mhdfstartmenu.sub_commands.reload.message")
                        .sendMessage(sender)
                    return false
                }

                "join" -> {
                    var player = sender as? Player
                    if (player == null && args.size == 2) {
                        player = Bukkit.getPlayer(args[1])
                        if (player == null) {
                            LangSetting.instance.i18n("player_offline").sendMessage(sender)
                            return false
                        }
                    }

                    if (player == null) {
                        LangSetting.instance.i18n("only_player").sendMessage(sender)
                        return false
                    }

                    player.joinMenu()
                    LangSetting.instance.i18n("commands.mhdfstartmenu.sub_commands.join.message")
                        .sendMessage(sender)
                    return false
                }

                "quit" -> {
                    var player = sender as? Player
                    if (player == null && args.size == 2) {
                        player = Bukkit.getPlayer(args[1])
                        if (player == null) {
                            LangSetting.instance.i18n("player_offline").sendMessage(sender)
                            return false
                        }
                    }

                    if (player == null) {
                        LangSetting.instance.i18n("only_player").sendMessage(sender)
                        return false
                    }

                    player.quitMenu()
                    LangSetting.instance.i18n("commands.mhdfstartmenu.sub_commands.quit.message")
                        .sendMessage(sender)
                    return false
                }
            }
        }

        // 输出帮助信息
        LangSetting.instance.i18n("commands.mhdfstartmenu.sub_commands.help.message")
            .replace(
                "{helpList}",
                LangSetting.instance.getHelpList("commands.mhdfstartmenu.sub_commands")
            )
            .replace("{command}", label)
            .sendMessage(sender)
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String> {
        if (args.size == 1) return LangSetting.instance.getKeys("commands.mhdfstartmenu.sub_commands").toList()
        return emptyList()
    }
}
