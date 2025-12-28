package cn.chengzhimeow.mhdfstartmenu.menu.item

import cn.chengzhiya.mhdfitem.MHDFItem

class ItemManager private constructor() {
    companion object {
        val instance by lazy { MHDFItem() }
    }
}