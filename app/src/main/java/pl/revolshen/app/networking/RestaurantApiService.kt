package pl.revolshen.app.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApiService {
    @GET("/restaurants/{restaurant_id}")
    fun getRestaurantById(@Path("restaurant_id") restaurantId: Int): Call<Restaurant>

    @GET("/restaurants/food_type/{food_type}")
    fun getRestaurantsByFoodType(@Path("food_type") foodType: String): Call<List<Restaurant>>

}

