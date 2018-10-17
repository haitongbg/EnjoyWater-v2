package com.enjoywater.retrofit;

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
import retrofit2.http.POST;

/**
 * Created by Tong Hai on 7/27/2017.
 * Email: haitongbg@gmail.com
 * Phone: +841688326555
 */

public interface MainService {
    @FormUrlEncoded
    @POST(Constants.Url.REGISTER)
    Call<RegisterResponse> register(@Field(Constants.Key.NAME) String name,
                                    @Field(Constants.Key.EMAIL) String email,
                                    @Field(Constants.Key.PHONE) String phone,
                                    @Field(Constants.Key.PASSWORD) String password);
    @FormUrlEncoded
    @POST(Constants.Url.LOGIN)
    Call<LoginResponse> login(@Field(Constants.Key.EMAIL) String email,
                              @Field(Constants.Key.PASSWORD) String password);

    //Factory
    class Factory {
        public static MainService create() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
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
