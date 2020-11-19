package kg.kyrgyzcoder.primedoc01.data.profile.model


import com.google.gson.annotations.SerializedName

data class Doctor(
    val categories: List<Category>,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val patronymic: String,
    val position: String,
    val username: String
)