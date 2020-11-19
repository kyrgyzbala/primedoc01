package kg.kyrgyzcoder.primedoc01.data.medcard


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModelCard(
    val birthDate: String,
    val firstName: String,
    val lastName: String,
    val medCardPhoneNumber: String,
    val patronymic: String,
    var userPhoto: String? = null
) : Serializable