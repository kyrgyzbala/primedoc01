package kg.kyrgyzcoder.primedoc01.data.payment


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModelPaymentOptions(
    val id: Int,
    val logo: String?,
    val name: String?,
    val nextSteps: String?,
    val paymentSteps: List<PaymentStep>
): Serializable