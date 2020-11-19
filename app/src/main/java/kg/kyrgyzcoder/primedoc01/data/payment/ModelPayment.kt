package kg.kyrgyzcoder.primedoc01.data.payment


import com.google.gson.annotations.SerializedName

data class ModelPayment(
    val authCode: String,
    val firstname: String,
    val last: String,
    val lastname: String,
    val oid: String,
    val sum: String,
    val totalSum: String,
    val type: String
)