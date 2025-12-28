package cn.chengzhimeow.mhdfstartmenu.condition

import cn.chengzhimeow.cccondition.CCCondition
import cn.chengzhimeow.cccondition.condition.ConditionBuilder
import cn.chengzhimeow.cccondition.exception.ConditionIllegalArgumentException
import cn.chengzhimeow.mhdfstartmenu.Main
import cn.chengzhimeow.mhdfstartmenu.util.array.join

fun ConditionBuilder.Builder.check(): List<String> {
    try {
        this.build().check()
        return emptyList()
    } catch (e: ConditionIllegalArgumentException) {
        return e.errorKeys.map { error ->
            when (error.caused) {
                ConditionIllegalArgumentException.ErrorCaused.NO_CAST_IMPLEMENTATION -> {
                    return@map "&c找不到参数${error.key.keys.join(0, ",")}的转换类实现!"
                }

                ConditionIllegalArgumentException.ErrorCaused.CAST_ERROR -> {
                    return@map "&c转换参数${
                        error.key.keys.join(
                            0,
                            ","
                        )
                    }时,遇到了一些问题!\n${error.e.stackTraceToString()}"
                }

                ConditionIllegalArgumentException.ErrorCaused.NOT_FOUND -> {
                    return@map "&c在参数列表中找不到参数${error.key.keys.join(0, ",")},参数数据: ${this.params}"
                }
            }
        }
    }
}

class ConditionManager private constructor() {
    companion object {
        val instance by lazy { CCCondition(Main.instance) }
    }
}