package com.example.tourister;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {


//    @POST("/signin")
//    Call<String> signInUser(@Field("username") String username,
//                            @Field("email") String email,
//                            @Field("phone") String phone);
//    @FormUrlEncoded
    @POST("/signin")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> signInUser(@Body MainUser mainUser);

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
//    @POST("/createteam")
//    Call<String> createteam(@Field("teamname") String teamname,
//                            @Field("username") String username,
//                            @Field("location") String location,
//                            @Field("phone") String phone);
    @POST("/createteam")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> createteam(@Body Team team);


    @POST("/jointeam")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<String> jointeam(@Body Team team);


    @FormUrlEncoded
    @POST("/teamname")
    Call<group> teamname(@Field("teamname") String teamname);

}
