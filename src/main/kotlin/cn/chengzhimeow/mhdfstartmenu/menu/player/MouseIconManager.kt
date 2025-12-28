package cn.chengzhimeow.mhdfstartmenu.menu.player

import cn.chengzhimeow.mhdfstartmenu.menu.thread.MouseIconThread
import cn.chengzhimeow.mhdfstartmenu.packet.entity.removeEntity
import cn.chengzhimeow.mhdfstartmenu.packet.entity.spawnPacketItemDisplay
import cn.chengzhimeow.mhdfstartmenu.packet.sendPacket
import cn.chengzhimeow.mhdfstartmenu.thread.CancellableRunnable
import com.github.retrooper.packetevents.protocol.world.Location
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.pow

data class IconMover(
    val icon: MouseIcon,
    var currentLocation: Location,
    var targetLocation: Location? = null
) {
    private var runnable: CancellableRunnable? = null

    fun start() {
        if (runnable != null) return

        runnable = object : CancellableRunnable() {
            // 用于追踪动画的起始点和总步数
            private var animationStartLocation: Location? = null
            private var activeTarget: Location? = null
            private var totalSteps = 0
            private var currentStep = 0

            private fun easeOutCubic(x: Double): Double {
                return 1 - (1 - x).pow(3.0)
            }

            override fun run() {
                // 目标更新
                if (targetLocation != null && activeTarget != targetLocation) {
                    animationStartLocation = currentLocation
                    activeTarget = targetLocation!!.clone()

                    val distance = currentLocation.position.distance(activeTarget!!.position)
                    totalSteps = max(1, ceil(distance / 0.08).toInt())
                    currentStep = 0
                }

                // 目标丢失
                if (activeTarget == null) {
                    end()
                    return
                }
                val target = activeTarget!!
                val start = animationStartLocation!!

                currentStep++

                val nextPosition: Vector3d
                val isFinalStep = currentStep >= totalSteps

                if (isFinalStep) {
                    // 如果是最后一步，直接将位置设置为目标位置
                    nextPosition = target.position
                } else {
                    // 使用带有缓动函数的线性插值计算当前帧的目标位置
                    val progress = currentStep.toDouble() / totalSteps
                    val easedProgress = easeOutCubic(progress)

                    val interpolatedX = start.x + (target.x - start.x) * easedProgress
                    val interpolatedY = start.y + (target.y - start.y) * easedProgress
                    val interpolatedZ = start.z + (target.z - start.z) * easedProgress
                    nextPosition = Vector3d(interpolatedX, interpolatedY, interpolatedZ)
                }

                // 始终使用相对移动来保证动画平滑
                WrapperPlayServerEntityRelativeMove(
                    icon.id,
                    nextPosition.x - currentLocation.x,
                    nextPosition.y - currentLocation.y,
                    nextPosition.z - currentLocation.z,
                    false
                ).sendPacket(icon.player)

                currentLocation = Location(nextPosition, currentLocation.yaw, currentLocation.pitch)

                // 如果动画结束，则清理状态并停止任务
                if (isFinalStep) {
                    targetLocation = null
                    activeTarget = null
                    animationStartLocation = null
                    end()
                }
            }
        }

        MouseIconThread.schedule(runnable!!, 0, 1)
    }

    fun end() {
        runnable?.cancel()
        runnable = null
    }
}

data class MouseIcon(
    val player: Player,
    val id: Int
) {
    lateinit var iconMover: IconMover
}

private val playerIcons = mutableMapOf<UUID, MouseIcon>()

fun Player.createMouseIcon(
    location: Location,
    itemStack: ItemStack
): MouseIcon {
    removeMouseIcon()

    val id = location.spawnPacketItemDisplay(
        this,
        itemStack
    )
    val icon = MouseIcon(this, id)
    icon.iconMover = IconMover(icon, location)
    playerIcons[this.uniqueId] = icon
    return icon
}

fun Player.getMouseIcon() = playerIcons[this.uniqueId]

fun Player.removeMouseIcon() {
    if (!playerIcons.contains(this.uniqueId)) return

    playerIcons.remove(this.uniqueId)?.let { icon ->
        icon.iconMover.end()
        icon.id.removeEntity(this)
    }
}

fun Player.moveMouseIcon(pos: Vector3d) {
    val icon = playerIcons[this.uniqueId] ?: return
    val mover = icon.iconMover

    val startLocation = mover.targetLocation ?: mover.currentLocation
    val endLocation = Location(
        startLocation.x + pos.x, startLocation.y + pos.y, startLocation.z + pos.z,
        startLocation.yaw, startLocation.pitch
    )
    mover.targetLocation = endLocation

    mover.start()
}
