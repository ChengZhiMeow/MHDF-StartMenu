package cn.chengzhimeow.mhdfstartmenu.packet.location

import io.github.retrooper.packetevents.util.SpigotConversionUtil
import org.bukkit.World
import com.github.retrooper.packetevents.protocol.world.Location as PacketLocation
import org.bukkit.Location as BukkitLocation

fun BukkitLocation.toPacketLocation() = SpigotConversionUtil.fromBukkitLocation(this)!!

fun PacketLocation.toBukkitLocation(world: World) = SpigotConversionUtil.toBukkitLocation(world, this)!!