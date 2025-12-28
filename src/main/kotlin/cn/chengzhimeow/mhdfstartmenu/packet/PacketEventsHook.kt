package cn.chengzhimeow.mhdfstartmenu.packet

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketListenerCommon
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import org.bukkit.entity.Player

fun Player.sendPacket(packet: PacketWrapper<*>) {
    PacketEvents.getAPI().playerManager.sendPacket(this, packet)
}

fun PacketWrapper<*>.sendPacket(player: Player) {
    player.sendPacket(this)
}

class PacketEventsHook private constructor() {
    companion object {
        val instance by lazy { PacketEventsHook() }
    }

    private var enable = false
    private val listenerList = mutableListOf<PacketListenerCommon>()

    /**
     * 初始化PacketEvents的API
     */
    fun hook() {
        this.enable = true
    }

    /**
     * 卸载PacketEvents的API
     */
    fun unhook() {
        this.unregisterListener()
        this.enable = false
    }

    /**
     * 注册监听器
     *
     * @param packetListener 数据包监听器实例
     * @param priority       监听 器权重
     */
    fun registerListener(
        packetListener: PacketListener?,
        priority: PacketListenerPriority?
    ) {
        this.listenerList.add(PacketEvents.getAPI().eventManager.registerListener(packetListener, priority))
    }

    /**
     * 取消注册所有监听器
     */
    fun unregisterListener() {
        this.listenerList.forEach {
            PacketEvents.getAPI().eventManager.unregisterListeners(it)
        }
    }
}