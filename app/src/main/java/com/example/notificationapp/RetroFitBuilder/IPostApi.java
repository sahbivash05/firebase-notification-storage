package com.example.notificationapp.RetroFitBuilder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IPostApi {

    @POST("/token/save")
    Call<Response> sendToken(@Body Response response);

}
