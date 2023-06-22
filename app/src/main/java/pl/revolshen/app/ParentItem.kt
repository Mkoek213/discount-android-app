package pl.revolshen.app

import android.graphics.Bitmap

data class ParentItem(
    val parentContent1: ParentContent,
    val parentContent2: ParentContent
)

data class ParentContent(
    val image : Int ,
    val title : String,
    val childItemList : List<ChildItem>,
    var isOpen : Boolean = false
)
data class ChildItem(val title: String, val imageUrl: String, val discount: String, val address: String, val time: String)
