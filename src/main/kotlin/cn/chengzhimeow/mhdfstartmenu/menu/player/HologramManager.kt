package cn.chengzhimeow.mhdfstartmenu.menu.player

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection
import cn.chengzhimeow.mhdfstartmenu.config.ConfigSetting
import cn.chengzhimeow.mhdfstartmenu.menu.thread.HologramThread
import de.oliver.fancyholograms.api.FancyHologramsPlugin
import de.oliver.fancyholograms.api.hologram.Hologram
import org.bukkit.entity.Player
import kotlin.jvm.optionals.getOrNull

private fun forEachHologram(
    player: Player,
    action: (key: String, config: ConfigurationSection, isHover: Boolean) -> Unit
) {
    val cameraLocation = player.getCameraLocation() ?: return
    val mouseIcon = player.getMouseIcon() ?: return
    val mouseLocation = mouseIcon.iconMover.currentLocation

    val holograms = ConfigSetting.instance.data.getConfigurationSection("hologram") ?: return
    holograms.keys.forEach { key ->
        val config = holograms.getConfigurationSection(key)!!
        val range = config.getConfigurationSection("range")!!

        val fromScreenY = range.getDouble("from.y")
        val toScreenY = range.getDouble("to.y")
        val fromScreenX = range.getDouble("from.x")
        val toScreenX = range.getDouble("to.x")

        val yaw = cameraLocation.yaw
        val isHover = mouseLocation.y in fromScreenY..toScreenY && when {
            yaw in 45.0..<135.0 -> mouseLocation.z in -fromScreenX..-toScreenX
            yaw >= 135 || yaw < -135 -> mouseLocation.x in fromScreenX..toScreenX
            yaw >= -135 && yaw < -45 -> mouseLocation.z in fromScreenX..toScreenX
            else -> mouseLocation.x in -fromScreenX..-toScreenX
        }
        action(key, config, isHover)
    }
}

private fun Hologram.show(player: Player) {
    if (!this.isViewer(player)) this.forceShowHologram(player)
}

private fun Hologram.hide(player: Player) {
    if (this.isViewer(player)) this.forceHideHologram(player)
}

val hologramManager = FancyHologramsPlugin.get().hologramManager!!

fun Player.updateMenuHologram() {
    HologramThread.execute {
        forEachHologram(this) { _, config, isHover ->
            val normalHologram = hologramManager.getHologram(config.getString("normal")).getOrNull()
            val hoverHologram = hologramManager.getHologram(config.getString("hover")).getOrNull()

            if (isHover && hoverHologram != null) {
                hoverHologram.show(this)
                normalHologram?.hide(this)
            } else {
                normalHologram?.show(this)
                hoverHologram?.hide(this)
            }
        }
    }
}

fun Player.clickMenuHologram() {
    HologramThread.execute {
        forEachHologram(this) { key, _, isHover ->
            if (!isHover) return@forEachHologram

            ConfigSetting.instance.actions[key]?.forEach { action ->
                val conditions = action.conditions.map { it.clone() }
                    .map {
                        it.params["user"] = this
                        it.params["player"] = this
                        it.params["placeholder_owner"] = this

                        val condition = it.build()
                        condition.init()
                        condition
                    }

                for (condition in conditions) {
                    if (!condition.checkCondition()) return@forEach
                }

                action.actions
                    .map { it.clone() }
                    .map {
                        it.params["player"] = this
                        it.params["placeholder_owner"] = this

                        val action = it.build()
                        action.init()
                        action
                    }.forEach { it.action() }
            }
        }
    }
}

fun Player.hideMenuHologram() {
    HologramThread.execute {
        forEachHologram(this) { _, config, _ ->
            val normalHologram = hologramManager.getHologram(config.getString("normal")).getOrNull()
            val hoverHologram = hologramManager.getHologram(config.getString("hover")).getOrNull()

            normalHologram?.hide(this)
            hoverHologram?.hide(this)
        }
    }
}