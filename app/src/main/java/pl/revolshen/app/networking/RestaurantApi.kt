package pl.revolshen.app.networking

import pl.revolshen.app.model.Restaurant
import retrofit2.Call
import retrofit2.http.GET

interface RestaurantApi {

    @GET("restaurants")
    fun getRestaurants(): Call<Restaurant>

}
