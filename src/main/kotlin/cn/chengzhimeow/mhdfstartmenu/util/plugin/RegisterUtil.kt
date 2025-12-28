package cn.chengzhimeow.mhdfstartmenu.util.plugin

import cn.chengzhimeow.mhdfstartmenu.config.LangSetting
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

fun JavaPlugin.registerListener(listener: Listener) {
    Bukkit.getPluginManager().registerEvents(listener, this)
}

fun JavaPlugin.registerCommand(
    command: String,
    permission: String? = null,
    executor: CommandExecutor
) {
    val commandConstructor =
        PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java)
    commandConstructor.isAccessible = true

    val cmd = commandConstructor.newInstance(command, this)
    cmd.permission = permission
    cmd.permissionMessage(LangSetting.instance.i18n("no_permission"))
    cmd.setExecutor(executor)
    if (executor is TabCompleter) cmd.tabCompleter = executor

    this.server.commandMap.register(this.description.name, cmd)
}