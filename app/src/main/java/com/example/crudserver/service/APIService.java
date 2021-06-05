package com.example.crudserver.service;

import com.example.crudserver.model.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @POST("/{token}/item/create")
    @FormUrlEncoded
    Call<Result> create(
            @Path("token") String token,
            @Field("name") String name,
            @Field("brand") String brand,
            @Field("price") Integer price
    );

    @POST("/{token}/item/{id}/update")
    @FormUrlEncoded
    Call<Result> update(
            @Path("token") String token,
            @Path("id") int id,
            @Field("name") String name,
            @Field("brand") String brand,
            @Field("price") Integer price
    );

    @GET("/{token}/item")
    Call<Result> getAll(
            @Path("token")String token
    );

    @GET("/{token}/item/{id}")
    Call<Result> getItem(
            @Path("token") String token,
            @Path("id") int id

    );

    @GET("/{token}/item/{id}/delete")
    Call<Result> delete(
            @Path("token") String token,
            @Path("id") int id

    );

}
