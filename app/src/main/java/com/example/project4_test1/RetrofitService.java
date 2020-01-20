package com.example.project4_test1;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("/persons/{userID}")
    Call<JsonArray> login(@Path("userID") String userID);

    @GET("/persons/{userID}")
    Call<JsonArray> validate(@Path("userID") String userID);

    @GET("/persons/{userID}")
    Call<JsonArray> getPersonInfo(@Path("userID") String userID);

    @FormUrlEncoded
    @PUT("/persons/{userID}")
    Call<JsonObject> setPersonInfo(@Field("userID") String userID,
                                      @Field("userBirth") String userBirth,
                                      @Field("userDisease") String userDisease,
                                      @Field("userAllergy") String userAllergy,
                                      @Field("userBloodtype") String userBloodtype,
                                      @Field("userHeight") String userHeight,
                                      @Field("userWeight") String userWeight,
                                      @Field("userEmerContact") String userEmerContact);

    @FormUrlEncoded
    @POST("/persons")
    Call<JsonPrimitive> register(@Field("userID") String userID,
                                 @Field("userPassword") String userPassword,
                                 @Field("name") String name,
                                 @Field("email") String email);
}
