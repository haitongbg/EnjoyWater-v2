package com.enjoywater.retrofit;

import android.support.annotation.Nullable;

import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.retrofit.response.LoginResponse;
import com.enjoywater.retrofit.response.RegisterResponse;
import com.enjoywater.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Tong Hai on 7/27/2017.
 * Email: haitongbg@gmail.com
 * Phone: +841688326555
 */

public interface MainService {
    //User
    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.LOGIN)
    Call<LoginResponse> login(@Field(Constants.Key.EMAIL) String email,
                              @Field(Constants.Key.PASSWORD) String password);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.REGISTER)
    Call<RegisterResponse> register(@Field(Constants.Key.NAME) String name,
                                    @Field(Constants.Key.EMAIL) String email,
                                    @Field(Constants.Key.PHONE) String phone,
                                    @Field(Constants.Key.PASSWORD) String password);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.ACTIVE_BY_CODE)
    Call<BaseResponse> activeByCode(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                    @Field(Constants.Key.CODE) String code);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.FORGET_PASSWORD)
    Call<BaseResponse> forgetPassword(@Field(Constants.Key.EMAIL) String email);

    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_USER_INFO)
    Call<RegisterResponse> getUserInfo(@Header(Constants.Key.TOKEN_LOGIN) String token);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.ACTIVE_MAIL)
    Call<BaseResponse> resendActiveMaild(@Field(Constants.Key.EMAIL) String email);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.UPDATE_USER_INFO)
    Call<BaseResponse> updateUserInfo(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                      @Nullable @Field(Constants.Key.NAME) String name,
                                      @Nullable @Field(Constants.Key.PASSWORD) String password,
                                      @Nullable @Field(Constants.Key.AVATAR) String avatar,
                                      @Nullable @Field(Constants.Key.GENDER) String gender,
                                      @Nullable @Field(Constants.Key.BIRTHDAY) String birthday,
                                      @Nullable @Field(Constants.Key.ADDRESS) String address,
                                      @Nullable @Field(Constants.Key.PASSPORT) String passport,
                                      @Nullable @Field(Constants.Key.PHONE) String phone,
                                      @Nullable @Field(Constants.Key.EMAIL) String email);

    // Home
    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_LIST_NEWS)
    Call<BaseResponse> getListNews(@Nullable @Header(Constants.Key.TOKEN_LOGIN) String token,
                                   @Nullable @Query(Constants.Key.LIMIT) String limit,
                                   @Nullable @Query(Constants.Key.PAGE) String page,
                                   @Nullable @Query(Constants.Key.SEARCH_KEY) String q);

    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_NEWS_DETAILS)
    Call<BaseResponse> getNewsDetails(@Nullable @Header(Constants.Key.TOKEN_LOGIN) String token,
                                      @Nullable @Query(Constants.Key.NEWS_ID) String newsId);

    //Factory
    class Factory {
        public static MainService create() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            OkHttpClient client = httpClient
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.Url.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(MainService.class);
        }
    }
}
