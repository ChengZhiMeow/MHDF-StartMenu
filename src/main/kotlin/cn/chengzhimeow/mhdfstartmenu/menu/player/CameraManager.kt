package cn.chengzhimeow.mhdfstartmenu.menu.player

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler
import cn.chengzhimeow.mhdfstartmenu.Main
import cn.chengzhimeow.mhdfstartmenu.config.ConfigSetting
import cn.chengzhimeow.mhdfstartmenu.menu.thread.MenuThread
import cn.chengzhimeow.mhdfstartmenu.packet.entity.removeEntity
import cn.chengzhimeow.mhdfstartmenu.packet.location.toPacketLocation
import cn.chengzhimeow.mhdfstartmenu.packet.sendPacket
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import io.github.retrooper.packetevents.util.SpigotReflectionUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import org.bukkit.Location as BukkitLocation

private val playerCameras = mutableMapOf<UUID, Int>()
private val cameraLocation = mutableMapOf<Int, BukkitLocation>()
private val originLocation = mutableMapOf<UUID, BukkitLocation>()

fun Player.inCamera() = playerCameras.contains(this.uniqueId)

fun Player.getCameraLocation(): BukkitLocation? {
    val cameraId = playerCameras[this.uniqueId] ?: return null
    return cameraLocation[cameraId]
}

fun Player.joinCamera(location: BukkitLocation) {
    quitCamera(false)
    originLocation.putIfAbsent(this.uniqueId, this.location)

    CCScheduler.getInstance().globalRegionScheduler.run {
        Bukkit.getOnlinePlayers().forEach {
            it.hidePlayer(Main.instance, this@joinCamera)
        }
    }

    val callback = this.teleportAsync(location)

    MenuThread.execute {
        callback.get()

        val cameraId = SpigotReflectionUtil.generateEntityId()
        cameraLocation.putIfAbsent(cameraId, location)
        WrapperPlayServerSpawnEntity(
            cameraId,
            UUID.randomUUID(),
            EntityTypes.INTERACTION,
            location.toPacketLocation(),
            0f,
            0,
            Vector3d()
        ).sendPacket(this)
        WrapperPlayServerChangeGameState(
            WrapperPlayServerChangeGameState.Reason.CHANGE_GAME_MODE,
            GameMode.SPECTATOR.ordinal.toFloat()
        ).sendPacket(this)
        WrapperPlayServerCamera(cameraId).sendPacket(this)
        WrapperPlayServerSetPassengers(cameraId, intArrayOf(this.entityId))
            .sendPacket(this)

        playerCameras[this.uniqueId] = cameraId
    }
}

fun Player.quitCamera(removeLocation: Boolean = true) {
    if (!playerCameras.contains(this.uniqueId)) return

    CCScheduler.getInstance().globalRegionScheduler.run {
        Bukkit.getOnlinePlayers().forEach {
            it.showPlayer(Main.instance, this@quitCamera)
        }
    }

    WrapperPlayServerCamera(this.entityId).sendPacket(this)

    playerCameras.remove(this.uniqueId)?.let {
        cameraLocation.remove(it)

        WrapperPlayServerSetPassengers(it, intArrayOf())
            .sendPacket(this)
        it.removeEntity(this)
    }

    WrapperPlayServerChangeGameState(
        WrapperPlayServerChangeGameState.Reason.CHANGE_GAME_MODE,
        SpigotConversionUtil.fromBukkitGameMode(this.gameMode).ordinal.toFloat()
    ).sendPacket(this)

    if (!removeLocation) return
    originLocation.remove(this.uniqueId)?.let {
        if (ConfigSetting.instance.data.getBoolean("back_location"))
            this.teleportAsync(it)
    }
}
