package com.example.weatherstation

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentConditionsZip(
        @Query("zip") zip: String,
        @Query("units") units: String = "Imperial",
        @Query("appid") appid: String = "6d3cbb90efe05909395139d822e2357b"
    ): CurrentConditions

    @GET("weather")
    suspend fun getCurrentConditionsLatLon(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "Imperial",
        @Query("appid") appid: String = "6d3cbb90efe05909395139d822e2357b"
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecastConditionsZip(
        @Query("zip") zip: String,
        @Query("units") units: String = "Imperial",
        @Query("appid") appid: String = "6d3cbb90efe05909395139d822e2357b",
        @Query("cnt") count: String = "16"
    ): Forecast

    @GET("forecast/daily")
    suspend fun getForecastConditionsLatLon(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "Imperial",
        @Query("appid") appid: String = "6d3cbb90efe05909395139d822e2357b",
        @Query("cnt") count: String = "16"
    ): Forecast

}