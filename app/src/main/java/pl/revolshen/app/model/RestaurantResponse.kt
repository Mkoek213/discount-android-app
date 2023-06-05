package pl.revolshen.app.model

import com.google.gson.annotations.SerializedName

data class RestaurantResponse(

	@field:SerializedName("Response")
	val response: List<ResponseItem?>? = null
)

data class ResponseItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("photo_url")
	val photoUrl: String? = null
)
