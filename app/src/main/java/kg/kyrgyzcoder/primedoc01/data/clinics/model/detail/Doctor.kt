package kg.kyrgyzcoder.primedoc01.data.clinics.model.detail


import com.google.gson.annotations.SerializedName

data class Doctor(
    val firstName: String?,
    val id: Int,
    val image: String?,
    val lastName: String?,
    val patronymic: String?,
    val position: String?
)