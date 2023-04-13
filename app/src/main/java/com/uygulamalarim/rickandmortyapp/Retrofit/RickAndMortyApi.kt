package com.uygulamalarim.rickandmortyapp.Retrofit

import com.uygulamalarim.rickandmortyapp.CharacterDetails
import com.uygulamalarim.rickandmortyapp.LocationModelMain
import com.uygulamalarim.rickandmortyapp.Model.LocationResidents
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): LocationModelMain
    @GET("location/{locationId}")
    suspend fun getLocationDetails(@Path("locationId") locationId: Int): LocationResidents
    @GET("character/{characterId}")
    suspend fun getCharacterDetails(@Path("characterId") characterId: Int): CharacterDetails
    @GET("character/{characterId}")
    suspend fun getMoreDetailCharacter(@Path("characterId") characterId: String): CharacterDetails
}