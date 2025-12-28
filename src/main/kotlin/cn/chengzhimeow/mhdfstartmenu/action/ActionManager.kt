package cn.chengzhimeow.mhdfstartmenu.action

import cn.chengzhimeow.ccaction.CCAction
import cn.chengzhimeow.ccaction.action.AbstractAction
import cn.chengzhimeow.ccaction.action.ActionBuilder
import cn.chengzhimeow.ccaction.action.ArgumentKey
import cn.chengzhimeow.ccaction.exception.CastException
import cn.chengzhimeow.ccaction.manager.CastManager
import cn.chengzhimeow.cccondition.condition.ConditionBuilder
import cn.chengzhimeow.cccondition.exception.ConditionIllegalArgumentException
import cn.chengzhimeow.mhdfstartmenu.Main
import cn.chengzhimeow.mhdfstartmenu.menu.quitMenu
import cn.chengzhimeow.mhdfstartmenu.util.array.join
import cn.chengzhimeow.mhdfstartmenu.util.message.color
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

fun ActionBuilder.Builder.check(): List<String> {
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
        }.toList()
    }
}

class Action(
    val conditions: List<ConditionBuilder.Builder> = emptyList(),
    val actions: List<ActionBuilder.Builder> = emptyList()
)

class QuitMenuAction(
    ccAction: CCAction,
    params: Map<String, Any>
) : AbstractAction(
    ccAction,
    params
) {
    @ArgumentKey(keys = ["player", "p"], disabledCheck = true)
    lateinit var player: Player

    override fun onAction() {
        player.quitMenu()
    }
}

class ComponentCastManager : CastManager {
    @Throws(CastException::class)
    override fun cast(
        ccAction: CCAction?,
        value: Any?,
        type: Class<*>
    ) = (value as? String)?.color() ?: CastException(value, type)
}

class ActionManager private constructor() {
    companion object {
        val instance by lazy {
            val action = CCAction(Main.instance)
            action.castRegistry.register(Component::class.java, ComponentCastManager())
            action.actionRegistry.register("quit", QuitMenuAction::class.java)
            action
        }
    }
}