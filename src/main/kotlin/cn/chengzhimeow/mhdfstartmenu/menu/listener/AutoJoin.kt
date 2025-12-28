package cn.chengzhimeow.mhdfstartmenu.menu.listener

import cn.chengzhimeow.mhdfstartmenu.config.ConfigSetting
import cn.chengzhimeow.mhdfstartmenu.menu.joinMenu
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class AutoJoin : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (ConfigSetting.instance.data.getBoolean("auto_join"))
            player.joinMenu()
    }
}