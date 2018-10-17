package com.enjoywater.retrofit;

import com.enjoywater.retrofit.response.RegisterResponse;
import com.enjoywater.utils.ConstantsAPI;

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
    @POST(ConstantsAPI.Url.REGISTER)
    Call<RegisterResponse> register(@Field(ConstantsAPI.Param.NAME) String name,
                                    @Field(ConstantsAPI.Param.EMAIL) String email,
                                    @Field(ConstantsAPI.Param.PHONE) String phone,
                                    @Field(ConstantsAPI.Param.PASSWORD) String password);

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
                    .baseUrl(ConstantsAPI.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(MainService.class);
        }
    }
}
