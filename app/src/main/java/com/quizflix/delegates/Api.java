package com.quizflix.delegates;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

/**
 * Created by Admin on 9/21/2015.
 */
public interface Api {

    int ADD_USER_REGISTRATION = 10001;

    @Headers("Content-Type: application/json")
    @POST("/account/Login")
    void userLogin(@Body TypedInput inmap, Callback<String> callback);

    @Headers("Content-Type: application/json")
    @POST("/account/Registeration")
    void userRegistreation(@Body TypedInput inmap, Callback<String> callback);

    @Headers("Content-Type: application/json")
    @POST("/User/GetQuestionData")
    void getQList(@Body TypedInput inmap, Callback<String> callback);

    @FormUrlEncoded
    @POST("/api.php")
    void getList(@Field("type") String type,
                 @Field("email") String email,
                 Callback<String> callback);

    @Multipart
    @POST("/api.php")
    void sendImage(@Part("type") String type,
                   @Part("email") String emailId,
                   @Part("imgurl") TypedFile file,
                   @Part("r_imgurl") String url,
                   @Part("tag") String tag,
                   @Part("fromemail") String fromemail,
                   Callback<String> callback);

    @Multipart
    @POST("/api.php")
    void sendSms(@Part("type") String type,
                 @Part("phone") String emailId,
                 @Part("imgurl") TypedFile file,
                 @Part("r_imgurl") String url,
                 @Part("tag") String tag,
                 @Part("fromemail") String fromemail,
                 Callback<String> callback);

    @FormUrlEncoded
    @POST("/api.php")
    void getImage(@Field("type") String type,
                  @Field("email") String email,
                  Callback<String> callback);

    @FormUrlEncoded
    @POST("/api.php")
    void getUserExist(@Field("type") String type,
                      @Field("email") String email,
                      @Field("device_id") String deviceId,
                      Callback<String> callback);
}

