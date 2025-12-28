package cn.chengzhimeow.mhdfstartmenu.menu.listener

import cn.chengzhimeow.mhdfstartmenu.menu.player.getCameraLocation
import cn.chengzhimeow.mhdfstartmenu.menu.player.inCamera
import cn.chengzhimeow.mhdfstartmenu.menu.player.moveMouseIcon
import cn.chengzhimeow.mhdfstartmenu.menu.player.updateMenuHologram
import cn.chengzhimeow.mhdfstartmenu.menu.thread.MenuThread
import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation
import org.bukkit.entity.Player
import java.util.*

data class MousePos(
    var yaw: Float,
    var pitch: Float
)

class MouseMove : PacketListener {
    companion object {
        val lastMousePosMap = mutableMapOf<UUID, MousePos>()
    }

    override fun onPacketReceive(event: PacketReceiveEvent) {
        if (event.packetType != PacketType.Play.Client.PLAYER_ROTATION) return
        val packet = WrapperPlayClientPlayerRotation(event)

        val player = event.getPlayer() as? Player ?: return
        if (!player.inCamera()) return
        val cameraLocation = player.getCameraLocation() ?: return

        val lastMousePos =
            lastMousePosMap.getOrPut(player.uniqueId) { MousePos(cameraLocation.yaw, cameraLocation.pitch) }
        val moveYaw = packet.yaw - lastMousePos.yaw
        val movePitch = packet.pitch - lastMousePos.pitch
        if (moveYaw == 0f && movePitch == 0f) return

        lastMousePos.yaw = packet.yaw
        lastMousePos.pitch = packet.pitch
        lastMousePosMap[player.uniqueId] = lastMousePos

        MenuThread.execute {
            // 移动鼠标图标
            run {
                // 像素转换为坐标系
                val moveScreenX = moveYaw / 16.0
                val moveScreenY = movePitch / 16.0

                var moveX = 0.0
                val moveY = -moveScreenY
                var moveZ = 0.0

                // 通过 YAW(-180f ~ 180f) 计算XZ坐标方向
                val yaw = cameraLocation.yaw
                when {
                    // West (-X): yaw in [45, 135)
                    yaw in 45.0..<135.0 -> moveZ = -moveScreenX
                    // North (-Z): yaw in [135, 180] or [-180, -135)
                    yaw >= 135 || yaw < -135 -> moveX = moveScreenX
                    // East (+X): yaw in [-135, -45)
                    yaw >= -135 && yaw < -45 -> moveZ = moveScreenX
                    // South (+Z): yaw in [-45, 45)
                    else -> moveX = -moveScreenX
                }

                // 移动鼠标指针
                player.moveMouseIcon(Vector3d(moveX, moveY, moveZ))
            }

            player.updateMenuHologram()
        }
    }
}