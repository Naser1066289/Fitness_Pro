package com.runora_dev.runoraf.Webservice;

import com.runora_dev.runoraf.Model.Responsedata;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v1/nutrition")
    Call<Responsedata> getItem(@Query("query") String queryy, @Header("X-Api-Key") String keyy);

}
