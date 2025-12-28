package cn.chengzhimeow.mhdfstartmenu.config

import cn.chengzhimeow.ccyaml.CCYaml
import cn.chengzhimeow.mhdfstartmenu.Main

class ConfigManager private constructor() {
    companion object {
        val instance: ConfigManager by lazy { ConfigManager() }
    }

    @Suppress("DEPRECATION")
    val yamlManager = CCYaml(
        Main::class.java.classLoader,
        Main.instance.dataFolder,
        Main.instance.description.version
    )
}