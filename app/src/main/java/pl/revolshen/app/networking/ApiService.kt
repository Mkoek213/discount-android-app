package pl.revolshen.app.networking

import pl.revolshen.app.model.RestaurantResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    // Get all restaurants
    @GET("restaurants") // Update the endpoint URL to match the API
    fun getRestaurants(): Call<RestaurantResponse>
}
