package kg.kyrgyzcoder.primedoc01.data.faq


import com.google.gson.annotations.SerializedName

data class ModelFaqItem(
    val answer: String,
    val id: Int,
    val order: Int,
    val question: String
)