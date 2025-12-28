package cn.chengzhimeow.mhdfstartmenu.menu

import cn.chengzhimeow.mhdfstartmenu.config.ConfigSetting
import cn.chengzhimeow.mhdfstartmenu.menu.item.ItemManager
import cn.chengzhimeow.mhdfstartmenu.menu.listener.MouseMove
import cn.chengzhimeow.mhdfstartmenu.menu.player.*
import cn.chengzhiya.mhdfitem.builder.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import com.github.retrooper.packetevents.protocol.world.Location as PacketLocation
import org.bukkit.Location as BukkitLocation

fun Player.joinMenu() {
    val cameraLocation = BukkitLocation(
        Bukkit.getWorld(ConfigSetting.instance.data.getString("location.world")!!),
        ConfigSetting.instance.data.getDouble("location.x"),
        ConfigSetting.instance.data.getDouble("location.y"),
        ConfigSetting.instance.data.getDouble("location.z"),
        ConfigSetting.instance.data.getFloat("location.yaw"),
        ConfigSetting.instance.data.getFloat("location.pitch")
    )

    val mouseIconLocation = PacketLocation(
        ConfigSetting.instance.data.getDouble("mouse_icon.location.x"),
        ConfigSetting.instance.data.getDouble("mouse_icon.location.y"),
        ConfigSetting.instance.data.getDouble("mouse_icon.location.z"),
        ConfigSetting.instance.data.getFloat("mouse_icon.location.yaw"),
        ConfigSetting.instance.data.getFloat("mouse_icon.location.pitch")
    )
    val mouseIconItem = ItemStackBuilder.builder(ItemManager.instance)
        .by(ConfigSetting.instance.data.getString("mouse_icon.item.by"))
        .type(ConfigSetting.instance.data.getString("mouse_icon.item.type"))
        .build()
    this.joinCamera(cameraLocation)
    this.updateMenuHologram()
    this.createMouseIcon(mouseIconLocation, mouseIconItem)
}

fun Player.quitMenu() {
    MouseMove.lastMousePosMap.remove(this.uniqueId)
    this.hideMenuHologram()
    this.removeMouseIcon()
    this.quitCamera()
}