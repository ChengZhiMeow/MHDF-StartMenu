package cn.chengzhimeow.mhdfstartmenu.packet.entity

import cn.chengzhimeow.mhdfstartmenu.packet.item.toPacketItemStack
import cn.chengzhimeow.mhdfstartmenu.packet.sendPacket
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.world.Location
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity
import io.github.retrooper.packetevents.util.SpigotReflectionUtil
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

fun Location.spawnPacketItemDisplay(
    player: Player,
    item: ItemStack,
    vararg data: EntityData<*>
): Int {
    @Suppress("UnstableApiUsage")
    val entityId = SpigotReflectionUtil.generateEntityId()
    WrapperPlayServerSpawnEntity(
        entityId,
        UUID.randomUUID(),
        EntityTypes.ITEM_DISPLAY,
        this,
        0f,
        0,
        Vector3d()
    ).sendPacket(player)

    WrapperPlayServerEntityMetadata(
        entityId,
        mutableListOf<EntityData<*>>(
            EntityData(
                23,
                EntityDataTypes.ITEMSTACK,
                item.toPacketItemStack()
            )
        ).apply { this.addAll(data) }
    ).sendPacket(player)

    return entityId
}

fun Int.removeEntity(player: Player) {
    WrapperPlayServerDestroyEntities(this).sendPacket(player)
}