package com.example.retrofit;

import com.example.retrofit.ModelResponse.DeleteAccountResponse;
import com.example.retrofit.ModelResponse.FetchUsersResponse;
import com.example.retrofit.ModelResponse.LoginResponse;
import com.example.retrofit.ModelResponse.RegisterResponse;
import com.example.retrofit.ModelResponse.UpdatePassResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("fetchusers.php")
    Call<FetchUsersResponse> fetchusers();

    @FormUrlEncoded
    @POST("updateuser.php")
    Call<LoginResponse> updateuser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("id") int id

    );

    @FormUrlEncoded
    @POST("updatepassword.php")
    Call<UpdatePassResponse> updatepassword(
            @Field("current") String currentpass,
            @Field("email") String email,
            @Field("new") String newpass

    );

    @FormUrlEncoded
    @POST("deleteaccount.php")
    Call<DeleteAccountResponse> deleteaccount(
            @Field("id") int id
    );

}
