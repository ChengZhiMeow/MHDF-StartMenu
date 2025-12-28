package cn.chengzhimeow.mhdfstartmenu

import cn.chengzhimeow.mhdfstartmenu.command.MHDFStartMenu
import cn.chengzhimeow.mhdfstartmenu.config.ConfigSetting
import cn.chengzhimeow.mhdfstartmenu.config.LangSetting
import cn.chengzhimeow.mhdfstartmenu.menu.listener.AutoJoin
import cn.chengzhimeow.mhdfstartmenu.menu.listener.AutoQuit
import cn.chengzhimeow.mhdfstartmenu.menu.listener.MouseClick
import cn.chengzhimeow.mhdfstartmenu.menu.listener.MouseMove
import cn.chengzhimeow.mhdfstartmenu.packet.PacketEventsHook
import cn.chengzhimeow.mhdfstartmenu.util.message.color
import cn.chengzhimeow.mhdfstartmenu.util.message.log
import cn.chengzhimeow.mhdfstartmenu.util.plugin.registerCommand
import cn.chengzhimeow.mhdfstartmenu.util.plugin.registerListener
import com.github.retrooper.packetevents.event.PacketListenerPriority
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
    }

    override fun onLoad() {
        instance = this

        if (!this.dataFolder.exists()) this.dataFolder.mkdir()
        ConfigSetting.instance.saveDefaultFile()
        ConfigSetting.instance.update()
        ConfigSetting.instance.reload()

        LangSetting.instance.saveDefaultFile()
        LangSetting.instance.update()
        LangSetting.instance.reload()
    }

    override fun onEnable() {
        PacketEventsHook.instance.hook()
        PacketEventsHook.instance.registerListener(MouseClick(), PacketListenerPriority.LOWEST)
        PacketEventsHook.instance.registerListener(MouseMove(), PacketListenerPriority.LOWEST)

        registerListener(AutoJoin())
        registerListener(AutoQuit())

        registerCommand("mhdfstartmenu", "mhdfstartmenu.commands.mhdfstartmenu", MHDFStartMenu())

        "&e-----------&6=&b梦之开始菜单&6=&e-----------".color().log()
        "&a插件启动成功! 作者: 292200693".color().log()
        "&e-----------&6=&b梦之开始菜单&6=&e-----------".color().log()
    }

    override fun onDisable() {
        PacketEventsHook.instance.unhook()

        "&e-----------&6=&b梦之开始菜单&6=&e-----------".color().log()
        "&a插件启动成功! 作者: 292200693".color().log()
        "&e-----------&6=&b梦之开始菜单&6=&e-----------".color().log()
    }
}
