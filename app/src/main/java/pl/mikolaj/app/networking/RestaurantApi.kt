package pl.mikolaj.app.networking

import pl.mikolaj.app.model.Restaurant
import retrofit2.Call
import retrofit2.http.GET

interface RestaurantApi {

    @GET("restaurants")
    fun getRestaurants(): Call<Restaurant>

}
