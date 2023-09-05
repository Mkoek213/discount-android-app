package pl.revolshen.app

import android.graphics.Bitmap

data class ParentItem(
    val parentContent1: ParentContent,
    val parentContent2: ParentContent
)
data class SearchParentItem(
    val searchContent1: SearchContent
)
data class SearchContent(
    val searchItemList : List<SearchItem>,

)

data class ParentContent(
    val image : Int ,
    val title : String,
    val childItemList : List<ChildItem>,
    var isOpen : Boolean = false
)
data class ChildItem(
    val name: String,
    val imageUrl: String,
    val address: String,
    val discount: String,
    val time: String,
    val language: String
    )
data class SearchItem(
    val name: String,
    val imageUrl: String,
    val address: String,
    val discount: String,
    val time: String,
    val language:String
)
