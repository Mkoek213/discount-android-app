package pl.revolshen.app.networking

data class Restaurant(
    val id: Int,
    val name: String,
    val address: String,
    val photo_url: String,
    val description: String,
    val food_type: String
)