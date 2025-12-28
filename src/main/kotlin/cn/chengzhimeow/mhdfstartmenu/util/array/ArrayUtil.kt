package cn.chengzhimeow.mhdfstartmenu.util.array

/**
 * 拼接为字符串
 *
 * @param index 开头指针
 * @param separator 间隔符
 * @return 拼接后的字符串
 */
fun Array<String>.join(
    index: Int = 0,
    separator: String = " "
): String {
    if (index >= this.size || index < 0) return ""

    val stringBuilder = StringBuilder("[")
    for (i in index until this.size) {
        stringBuilder.append(this[i])
        if (i < this.size - 1) stringBuilder.append(separator)
    }
    stringBuilder.append("]")

    return stringBuilder.toString()
}