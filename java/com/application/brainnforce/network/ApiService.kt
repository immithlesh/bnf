package com.application.brainnforce.network

import com.application.brainnforce.model.*
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @Multipart
    @POST("registerUserBuyer")
    fun registerUserBuyer(
        @Part("payload") payload: RequestBody,
        @Part profile: MultipartBody.Part?,
        @Part id: MultipartBody.Part?
    ): Single<Any>

    @Multipart
    @POST("registerUserSeller")
    fun registerUserSeller(
        @Part("payload") payload: SignUpSellerRequestModel,
        @Part profilePicture: MultipartBody.Part?,
        @Part idProof: MultipartBody.Part?,
        @Part coverVideo: MultipartBody.Part?
    ): Single<Any>

    @POST("login")
    fun apiLogin(@Body body: LoginRequestModel): Single<LoginCrediental>

    @POST("changePassword")
    fun apiChangePassword(@Body body: changePassword): Single<Any>

    @POST("sendOtp")
    fun apiSendOtp(@Body body: RequestBody): Single<Any>

    @POST("verifyOtp")
    fun apiVerifyOtp(@Body body: RequestBody): Single<Any>

    @POST("resetPassword")
    fun apiResetPassword(@Body body: RequestBody): Single<Any>

    @GET("category")
    fun apiCategory(): Single<MainCategory>

    @POST("userPackage")
    fun apiuserPackage(@Body body: EditCategory): Single<Any>

    @GET("getUserInfo")
    fun apiGetUserInfo(): Single<LoginCrediental>

    @Multipart
    @POST("createProfileBuyer")
    fun createProfileBuyer(
        @Part("payload") payload: RequestBody,
        @Part profile: MultipartBody.Part?,
        @Part id: MultipartBody.Part?
    ): Single<Any>

    @Multipart
    @POST("createProfileSeller")
    fun createProfileSeller(
        @Part("payload") payload: SignUpSellerRequestModel,
        @Part profilePicture: MultipartBody.Part?,
        @Part idProof: MultipartBody.Part?,
        @Part coverVideo: MultipartBody.Part?
    ): Single<Any>

    @DELETE("userPackage/{user_id}")
    fun apiDeletePackage(@Path("user_id") userId: Int?): Single<Any>

    @PUT("userPackage")
    fun apiUpdatePackage(@Body body: EditCategory): Single<Any>


    @Multipart
    @POST("addCoverImage")
    fun addCoverImage(
        @Part coverImage: MultipartBody.Part?
    ): Single<Any>


    @GET("seller")
    fun apiGetSellerList(): Single<SellerList>

    @POST("socialLogin")
    fun socialLoginApi(@Body jsonObject: JsonObject): Single<LoginCrediental>

}
