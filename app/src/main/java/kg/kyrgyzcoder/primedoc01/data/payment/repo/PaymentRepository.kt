package kg.kyrgyzcoder.primedoc01.data.payment.repo

import kg.kyrgyzcoder.primedoc01.data.payment.ModelPayment
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentOptions
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentResponse
import retrofit2.Response

interface PaymentRepository {

    suspend fun getPaymentOptions(token: String): List<ModelPaymentOptions>

    suspend fun sendPaymentSuccess(
        token: String,
        resId: Int,
        model: ModelPayment
    ): Response<ModelPaymentResponse>
}