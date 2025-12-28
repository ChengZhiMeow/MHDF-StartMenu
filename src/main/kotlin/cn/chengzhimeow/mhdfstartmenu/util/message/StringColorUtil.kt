package cn.chengzhimeow.mhdfstartmenu.util.message

import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor


/**
 * 将旧版颜色符号转换为minimessage颜色符号
 */
fun String.toMiniMessageString() = legacyHexToMiniMessage(legacyToMiniMessage(legacy(this)))

/**
 * 解析颜色
 */
fun String.color() = MiniMessage.miniMessage().deserialize("<!i>" + this.toMiniMessageString())

/**
 * 旧版彩色符号(例如: &e)处理
 *
 * @param legacy 旧版颜色字符文本
 * @return 处理后的文本
 */
private fun legacy(legacy: String) = ChatColor.translateAlternateColorCodes('&', legacy)

/**
 * 将旧版RGB颜色字符文本转换为miniMessage格式
 *
 * @param legacy 旧版颜色字符文本
 * @return miniMessage格式文本
 */
private fun legacyHexToMiniMessage(legacy: String) = legacy
    .replace("&#", "#")
    .replace("(?!:)(?<!<)#([0-9a-fA-F]{6})(?!>)(?!:)".toRegex(), "<#$1>")

/**
 * 检测字符是否是颜色代码的字符
 *
 * @param c 字符
 * @return 结果
 */
private fun isColorCode(c: Char) = c != '§' && c != '&'

/**
 * 将旧版颜色字符文本转换为miniMessage格式
 *
 * @param legacy 旧版颜色字符文本
 * @return miniMessage格式文本
 */
private fun legacyToMiniMessage(legacy: String): String {
    val stringBuilder = StringBuilder()
    val chars = legacy.toCharArray()
    var i = 0
    while (i < chars.size) {
        if (isColorCode(chars[i])) {
            stringBuilder.append(chars[i])
            i++
            continue
        }
        if (i + 1 >= chars.size) {
            stringBuilder.append(chars[i])
            i++
            continue
        }
        when (chars[i + 1]) {
            '0' -> stringBuilder.append("<black>")
            '1' -> stringBuilder.append("<dark_blue>")
            '2' -> stringBuilder.append("<dark_green>")
            '3' -> stringBuilder.append("<dark_aqua>")
            '4' -> stringBuilder.append("<dark_red>")
            '5' -> stringBuilder.append("<dark_purple>")
            '6' -> stringBuilder.append("<gold>")
            '7' -> stringBuilder.append("<gray>")
            '8' -> stringBuilder.append("<dark_gray>")
            '9' -> stringBuilder.append("<blue>")
            'a' -> stringBuilder.append("<green>")
            'b' -> stringBuilder.append("<aqua>")
            'c' -> stringBuilder.append("<red>")
            'd' -> stringBuilder.append("<light_purple>")
            'e' -> stringBuilder.append("<yellow>")
            'f' -> stringBuilder.append("<white>")
            'r' -> stringBuilder.append("<reset>")
            'l' -> stringBuilder.append("<b>")
            'm' -> stringBuilder.append("<st>")
            'o' -> stringBuilder.append("<i>")
            'n' -> stringBuilder.append("<u>")
            'k' -> stringBuilder.append("<obf>")
            'x' -> {
                if (i + 13 >= chars.size || isColorCode(chars[i + 2])
                    || isColorCode(chars[i + 4])
                    || isColorCode(chars[i + 6])
                    || isColorCode(chars[i + 8])
                    || isColorCode(chars[i + 10])
                    || isColorCode(chars[i + 12])
                ) {
                    stringBuilder.append(chars[i])
                    i++
                    continue
                }
                stringBuilder
                    .append("<#")
                    .append(chars[i + 3])
                    .append(chars[i + 5])
                    .append(chars[i + 7])
                    .append(chars[i + 9])
                    .append(chars[i + 11])
                    .append(chars[i + 13])
                    .append(">")
                i += 12
            }

            else -> {
                stringBuilder.append(chars[i])
                i++
                continue
            }
        }
        i++
        i++
    }
    return stringBuilder.toString()
}