package kg.kyrgyzcoder.primedoc01.data.payment.repo

import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPayment
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentOptions
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentResponse
import retrofit2.Response

class PaymentRepositoryImpl(private val apiService: ApiService) : PaymentRepository {

    override suspend fun getPaymentOptions(token: String): List<ModelPaymentOptions> {
        return apiService.getPaymentOptionsAsync(token).await()
    }

    override suspend fun sendPaymentSuccess(
        token: String,
        resId: Int,
        model: ModelPayment
    ): Response<ModelPaymentResponse> {
        return apiService.sendPaymentSuccessAsync(token, resId, model).await()
    }
}