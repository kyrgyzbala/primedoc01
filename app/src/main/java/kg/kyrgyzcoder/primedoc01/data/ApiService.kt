package kg.kyrgyzcoder.primedoc01.data

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kg.kyrgyzcoder.primedoc01.data.aboutUs.ModelDoc
import kg.kyrgyzcoder.primedoc01.data.aboutUs.model.ModelAboutUs
import kg.kyrgyzcoder.primedoc01.data.clinics.model.ModelCategory
import kg.kyrgyzcoder.primedoc01.data.clinics.model.detail.ModelCategDetail
import kg.kyrgyzcoder.primedoc01.data.clinics.model.doctor.ModelDoctorInfo
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.ModelMakeReservation
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response.ModelReservResponse
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.ModelWorkTime
import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaqItem
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelCard
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelUserPhotoResponse
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPayment
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentOptions
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentResponse
import kg.kyrgyzcoder.primedoc01.data.profile.model.ModelReservationH
import kg.kyrgyzcoder.primedoc01.data.register.*
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateToken
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateTokenResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*


interface ApiService {


    @POST("v1/register")
    fun registerAsync(@Body model: ModelRegister): Deferred<Response<ModelRegister>>

    @POST("v1/verify/{token}")
    fun verifyCodeAsync(@Path("token") token: String): Deferred<Response<ModelRegister>>

    @POST("v1/auth")
    fun loginWithPwdAsync(@Body model: ModelLogin): Deferred<Response<ModelLoginResponse>>

    @POST("v1/recovery/{phone}")
    fun recoverPwdAsync(@Path("phone") phoneNumber: String): Deferred<Response<ModelRecoveryResponse>>

    @POST("v1/reset/")
    fun resetPwdAsync(@Body modelResetPwd: ModelResetPwd): Deferred<Response<ModelResetResponse>>

    @GET("v1/category/")
    fun getCategoriesAsync(@Header("Authorization") token: String): Deferred<ModelCategory>

    @GET("v1/category/info/details/{id}")
    fun getCategoryDetailAsync(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Deferred<ModelCategDetail>

    @GET("v1/doctor/profile/{id}")
    fun getDoctorProfileAsync(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Deferred<ModelDoctorInfo>

    @GET("/api/v1/worktime/relevant/{id}")
    fun getDoctorWorkTimeAsync(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Deferred<ModelWorkTime>

    @PUT("v1/worktime/reserve")
    fun makeReservationAsync(
        @Header("Authorization") token: String,
        @Body modelReserve: ModelMakeReservation
    ): Deferred<Response<ModelReservResponse>>

    @GET("v1/faq/")
    fun getFaqListAsync(@Header("Authorization") token: String): Deferred<List<ModelFaqItem>>

    @GET("v1/about/")
    fun getAboutUsTextAsync(@Header("Authorization") token: String): Deferred<ModelAboutUs>

    @PUT("v1/client/card/{id}")
    fun putUserMedCardDataAsync(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
        @Body model: ModelCard
    ): Deferred<Response<ModelCard>>

    @Multipart
    @PUT("v1/client/{id}")
    fun putUserPhotoAsync(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
        @Part imageFile: MultipartBody.Part?
    ): Deferred<Response<ModelUserPhotoResponse>>

    @GET("v1/client/{id}")
    fun getUserPhotoAsync(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Deferred<ModelUserPhotoResponse>


    @GET("v1/client/card/{id}")
    fun getUserCardAsync(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Deferred<ModelCard>

    @GET("v1/payment/")
    fun getPaymentOptionsAsync(@Header("Authorization") token: String): Deferred<List<ModelPaymentOptions>>

    @GET("v1/reservation/current/{userId}")
    fun getUpcomingReservationsAsync(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Deferred<List<ModelReservationH>>

    @GET("v1/reservation/approved/{userId}")
    fun getConfirmedReservationsAsync(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Deferred<List<ModelReservationH>>

    @GET("v1/reservation/made/{userId}")
    fun getReservationsDoneAsync(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Deferred<List<ModelReservationH>>

    @PUT("v1/reservation/pay/{resId}")
    fun sendPaymentSuccessAsync(
        @Header("Authorization") token: String,
        @Path("resId") resId: Int,
        @Body model: ModelPayment
    ): Deferred<Response<ModelPaymentResponse>>

    @PUT("v1/user/fb/")
    fun sendFcmTokenAsync(
        @Header("Authorization") token: String,
        @Body model: ModelUpdateToken
    ): Deferred<Response<ModelUpdateTokenResponse>>


    @GET("v1/docs/html/CONTRACT_OFFER")
    fun getContractOfferAsync(
        @Header("Authorization") token: String
    ): Deferred<String>

    @GET("v1/docs/html/PERSONAL_DATA")
    fun getPersonalDataAsync(
        @Header("Authorization") token: String
    ): Deferred<String>

    @GET("v1/docs/html/PAID_SERVICE")
    fun getPaidServiceAsync(
        @Header("Authorization") token: String
    ): Deferred<String>


    companion object {
        operator fun invoke(interceptor: ConnectivityInterceptorImpl): ApiService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(logging)
                .addInterceptor(interceptor).build()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://primedocapp.one:8443/api/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }
}