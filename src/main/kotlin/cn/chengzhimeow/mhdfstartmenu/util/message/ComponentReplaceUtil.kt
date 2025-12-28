package cn.chengzhimeow.mhdfstartmenu.util.message

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig

fun Component.replace(
    target: String,
    replacement: String
) =
    this.replaceText(
        TextReplacementConfig.builder()
            .matchLiteral(target)
            .replacement(replacement)
            .build()
    )

fun Component.replace(
    target: String,
    replacement: Component
) =
    this.replaceText(
        TextReplacementConfig.builder()
            .matchLiteral(target)
            .replacement(replacement)
            .build()
    )

fun Component.replaceFirst(
    target: String,
    replacement: String
) =
    this.replaceText(
        TextReplacementConfig.builder()
            .matchLiteral(target)
            .replacement(replacement)
            .times(1)
            .build()
    )

fun Component.replaceFirst(
    target: String,
    replacement: Component
) =
    this.replaceText(
        TextReplacementConfig.builder()
            .matchLiteral(target)
            .replacement(replacement)
            .times(1)
            .build()
    )