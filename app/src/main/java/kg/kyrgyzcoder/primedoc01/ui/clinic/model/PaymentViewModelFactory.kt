package kg.kyrgyzcoder.primedoc01.ui.clinic.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kg.kyrgyzcoder.primedoc01.data.payment.repo.PaymentRepository

class PaymentViewModelFactory(private val paymentRepository: PaymentRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PaymentViewModel(paymentRepository) as T
    }
}