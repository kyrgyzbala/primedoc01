package kg.kyrgyzcoder.primedoc01.data.payment


import com.google.gson.annotations.SerializedName

data class ModelPaymentResponse(
    val bill: String,
    val checkUrl: String,
    val comment: String,
    val end: String,
    val id: Int,
    val paid: Boolean,
    val phoneNumber: String,
    val start: String
)