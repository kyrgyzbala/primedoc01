package kg.kyrgyzcoder.primedoc01.data.payment


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentStep(
    val id: Int,
    val number: Int,
    val text: String
) : Serializable