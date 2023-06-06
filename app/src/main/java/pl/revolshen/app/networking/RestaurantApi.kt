package pl.revolshen.app.networking

import pl.revolshen.app.model.Character
import retrofit2.Call
import retrofit2.http.GET

interface CharacterApi {
    @GET("character/18") // Endpoint for the specific character with ID 18
    fun getCharacter(): Call<Character>
}
