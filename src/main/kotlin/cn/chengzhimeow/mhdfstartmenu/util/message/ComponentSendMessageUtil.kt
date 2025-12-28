package cn.chengzhimeow.mhdfstartmenu.util.message

import cn.chengzhimeow.mhdfstartmenu.config.ConfigSetting
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

private val CONSOLE_PREFIX = Component.text("[MHDF-StartMenu] ")
private val DEBUG_PREFIX = Component.text("[MHDF-StartMenu-调试] ")

fun Component.sendMessage(target: Audience) {
    target.sendMessage(this)
}

fun Component.log() {
    CONSOLE_PREFIX.append(this)
        .sendMessage(Bukkit.getConsoleSender())
}


fun Component.debug() {
    if (!ConfigSetting.instance.data.getBoolean("debug")) return
    DEBUG_PREFIX.append(this)
        .sendMessage(Bukkit.getConsoleSender())
}
