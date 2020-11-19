package kg.kyrgyzcoder.primedoc01.ui.clinic.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPayment
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentOptions
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentResponse
import kg.kyrgyzcoder.primedoc01.data.payment.repo.PaymentRepository
import retrofit2.Response

class PaymentViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {

    suspend fun getPaymentOptions(token: String): List<ModelPaymentOptions> {
        return paymentRepository.getPaymentOptions(token)
    }

    suspend fun sendPaymentSuccess(
        token: String,
        resId: Int,
        model: ModelPayment
    ): Response<ModelPaymentResponse> {
        return paymentRepository.sendPaymentSuccess(token, resId, model)
    }
}