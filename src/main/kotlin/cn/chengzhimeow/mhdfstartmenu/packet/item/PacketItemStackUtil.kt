package cn.chengzhimeow.mhdfstartmenu.packet.item

import io.github.retrooper.packetevents.util.SpigotConversionUtil
import org.bukkit.inventory.ItemStack

fun ItemStack.toPacketItemStack() = SpigotConversionUtil.fromBukkitItemStack(this)!!