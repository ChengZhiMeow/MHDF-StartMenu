package cn.chengzhimeow.mhdfstartmenu.menu.listener

import cn.chengzhimeow.mhdfstartmenu.menu.player.clickMenuHologram
import cn.chengzhimeow.mhdfstartmenu.menu.player.inCamera
import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import org.bukkit.entity.Player

class MouseClick : PacketListener {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        if (event.packetType != PacketType.Play.Client.INTERACT_ENTITY) return
        event.isCancelled = true

        val packet = WrapperPlayClientInteractEntity(event)
        if (packet.action != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return

        val player = event.getPlayer() as? Player ?: return
        if (!player.inCamera()) return

        player.clickMenuHologram()
    }
}