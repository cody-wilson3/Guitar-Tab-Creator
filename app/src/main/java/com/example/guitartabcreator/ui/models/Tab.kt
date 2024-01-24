package com.example.guitartabcreator.ui.models

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Tab (
    val id: String? = null,
    val userId: String? = null,
    val tabName: String? = null,
    val fretItems: List<RowOfStrings>? = null
)

data class TabStringItem(
    val whichItem: String,
    val stringNumberNumber: Int,
    var offSetX: Dp,
    val whichThird: Int
    ) {
    // Secondary constructor for a no-argument constructor
    constructor() : this("", 0, 0.dp, 0)
}

data class OneThirdString(
    var thirdNum: Int = 0,
    var tabStringItems: MutableList<TabStringItem> = mutableListOf()
)

data class SingleString(
    var stringNumber: Int = 0,
    val oneThirdsList: List<OneThirdString> = List(3) {index ->
        OneThirdString(thirdNum = index + 1)
    }

)

data class RowOfStrings(
    val stringList: List<SingleString> = List(6) { index ->
        SingleString(stringNumber = index + 1)
    },
    var rowNumber: Int = 0
) {
    fun addTabStringItem(tabStringItem: TabStringItem) {
       stringList[tabStringItem.stringNumberNumber.minus(1)]
           .oneThirdsList[tabStringItem.whichThird.minus(1)]
           .tabStringItems.add(tabStringItem)
        }
}
