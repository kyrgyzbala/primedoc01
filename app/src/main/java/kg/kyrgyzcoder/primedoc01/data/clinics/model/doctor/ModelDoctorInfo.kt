package kg.kyrgyzcoder.primedoc01.data.clinics.model.doctor


import com.google.gson.annotations.SerializedName

data class ModelDoctorInfo(
    val bio: String?,
    val firstName: String?,
    val id: Int,
    val user_id: Int,
    val image: String?,
    val information: List<Information>?,
    val lastName: String?,
    val patronymic: String?,
    val position: String?,
    val username: String?
)