package kg.kyrgyzcoder.primedoc01.data.register.fcmtoken


import com.google.gson.annotations.SerializedName

data class ModelUpdateTokenResponse(
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<String>,
    val birthDate: String,
    val credentialsNonExpired: Boolean,
    val enabled: Boolean,
    val firebaseToken: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val password: String,
    val patronymic: String,
    val username: String,
    val verification: String
)