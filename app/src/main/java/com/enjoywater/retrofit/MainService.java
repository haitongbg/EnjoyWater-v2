package com.enjoywater.retrofit;

import com.enjoywater.retrofit.response.BaseResponse;
import com.enjoywater.retrofit.response.GoogleOAuthResponse;
import com.enjoywater.utils.Constants;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    Call<BaseResponse> login(@Field(Constants.Key.EMAIL) String email,
                             @Field(Constants.Key.PASSWORD) String password);

    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.LOGIN_BY_TOKEN)
    Call<BaseResponse> loginByToken(@Header(Constants.Key.TOKEN_LOGIN) String token);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.LOGIN_BY_SOCIAL_ACCESS_TOKEN)
    Call<BaseResponse> loginBySocialAccessToken(@Field(Constants.Key.SOCIAL_TYPE) String socialType,
                                                @Field(Constants.Key.ACCESS_TOKEN) String accessToken);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.REGISTER)
    Call<BaseResponse> register(@Field(Constants.Key.NAME) String name,
                                @Field(Constants.Key.EMAIL) String email,
                                @Field(Constants.Key.PHONE) String phone,
                                @Field(Constants.Key.PASSWORD) String password);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.REGISTER_DEVICE)
    Call<BaseResponse> registerDevice(@Field(Constants.Key.USER_ID) String userId,
                                      @Field(Constants.Key.DEVICE_ID) String deviceId,
                                      @Field(Constants.Key.TOKEN) String deviceToken,
                                      @Field(Constants.Key.PLATFORM) String platform,
                                      @Field(Constants.Key.ENABLE_NOTIFICATION) boolean enableNotification);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.GET_ACTIVE_CODE)
    Call<BaseResponse> getActiveCode(@Field(Constants.Key.EMAIL) String email);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.ACTIVE_BY_CODE)
    Call<BaseResponse> activeByCode(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                    @Field(Constants.Key.CODE) String code);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.FORGET_PASSWORD)
    Call<BaseResponse> forgetPassword(@Field(Constants.Key.EMAIL) String email);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.RESET_PASSWORD)
    Call<BaseResponse> resetPassword(@Field(Constants.Key.EMAIL) String email,
                                     @Field(Constants.Key.CODE) String code,
                                     @Field(Constants.Key.NEW_PASSWORD) String newPassword,
                                     @Field(Constants.Key.NEW_PASSWORD_REPEATED) String newPasswordRepeated);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.UPDATE_USER_INFO)
    Call<BaseResponse> changePassword(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                      @Field(Constants.Key.PASSWORD) String password);

    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_USER_INFO)
    Call<BaseResponse> getUserInfo(@Header(Constants.Key.TOKEN_LOGIN) String token);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.GET_ACTIVE_CODE)
    Call<BaseResponse> resendActiveMail(@Field(Constants.Key.EMAIL) String email);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.UPDATE_USER_INFO)
    Call<BaseResponse> updateUserInfo(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                      @Field(Constants.Key.NAME) String name,
                                      @Field(Constants.Key.GENDER) String gender,
                                      @Field(Constants.Key.BIRTHDAY) String birthday,
                                      @Field(Constants.Key.PHONE) String phone);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.ADD_NEW_ADDRESS)
    Call<BaseResponse> addNewAddress(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                     @Field(Constants.Key.NAME) String name,
                                     @Field(Constants.Key.PHONE) String phone,
                                     @Field(Constants.Key.CITY_ID) String provinceId,
                                     @Field(Constants.Key.DISTRICT_ID) String districtId,
                                     //@Field(Constants.Key.WARD_ID) String wardId,
                                     @Field(Constants.Key.ADDRESS) String address,
                                     @Field(Constants.Key.IS_DEFAULT) int isDefault);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.SUBMIT_REF_CODE)
    Call<BaseResponse> submitRefCode(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                      @Field(Constants.Key.CODE) String code);

    //Home
    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_LIST_NEWS)
    Call<BaseResponse> getListNews(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                   @Query(Constants.Key.LIMIT) int limit,
                                   @Query(Constants.Key.PAGE) int page,
                                   @Query(Constants.Key.TYPE) String type);

    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_NEWS_DETAILS)
    Call<BaseResponse> getNewsDetails(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                      @Path(Constants.Key.NEWS_ID) int newsId);

    //Product
    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_LIST_PRODUCTS)
    Call<BaseResponse> getListProducts(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                       @Query(Constants.Key.LIMIT) int limit,
                                       @Query(Constants.Key.PAGE) int page);

    //Order
    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_LIST_ORDER)
    Call<BaseResponse> getOrderHistory(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                       @Query(Constants.Key.LIMIT) int limit,
                                       @Query(Constants.Key.PAGE) int page);

    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_ORDER_DETAILS)
    Call<BaseResponse> getOrderDetails(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                       @Path(Constants.Key.ORDER_ID) int orderId);

    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.CREATE_ORDER)
    Call<BaseResponse> createOrder(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                   @Body JsonObject order);

    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.CANCEL_ORDER)
    Call<BaseResponse> cancelOrder(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                   @Path(Constants.Key.ORDER_ID) int orderId);

    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.CONFIRM_RECEIVED)
    Call<BaseResponse> confirmReceived(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                       @Path(Constants.Key.ORDER_ID) int orderId);

    //promotion
    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_COUPON_DETAILS)
    Call<BaseResponse> getCouponDetails(@Path(Constants.Key.CODE) String code);

    //notification
    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_LIST_NOTIF)
    Call<BaseResponse> getListNotify(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                     @Query(Constants.Key.LIMIT) int limit,
                                     @Query(Constants.Key.PAGE) int page);

    @Headers(Constants.Value.SECRET_HEADER)
    @GET(Constants.Url.GET_NOTIF_DETAILS)
    Call<BaseResponse> getNotifyDetails(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                        @Path(Constants.Key.NOTIFY_ID) int notifyId);

    @FormUrlEncoded
    @Headers(Constants.Value.SECRET_HEADER)
    @POST(Constants.Url.UPDATE_STATUS_NOTIF)
    Call<BaseResponse> updateNotifyStatus(@Header(Constants.Key.TOKEN_LOGIN) String token,
                                          @Path(Constants.Key.NOTIFY_ID) int notifyId,
                                          @Field(Constants.Key.STATUS) String status);

    //Google
    @FormUrlEncoded
    @POST(Constants.Url.GOOGLE_OAUTH2_TOKEN)
    Call<GoogleOAuthResponse> getAccessToken(@Field(Constants.Key.CLIENT_ID) String clientId,
                                             @Field(Constants.Key.CLIENT_SECRET) String clientSecret,
                                             @Field(Constants.Key.GRANT_TYPE) String grant_type,
                                             @Field(Constants.Key.CODE) String code,
                                             @Field(Constants.Key.REFRESH_TOKEN) String refreshToken);

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
