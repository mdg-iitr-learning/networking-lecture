package com.sachan.prateek.retrofitsample;

import com.sachan.prateek.retrofitsample.models.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("/data/2.5/weather")
    Call<Data> getWeatherData(@Query("appid")String apiKey, @Query("zip")String zipCode);
}
