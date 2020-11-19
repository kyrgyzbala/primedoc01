package kg.kyrgyzcoder.primedoc01.data.medcard.model


import com.google.gson.annotations.SerializedName

data class Content(
    val birthDate: String,
    val firstName: String,
    val id: Int,
    val image: String,
    val lastName: String,
    val patronymic: String,
    val username: String
)