package kg.kyrgyzcoder.primedoc01.data.register


import com.google.gson.annotations.SerializedName

data class ModelResetResponse(
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<String>,
    val birthDate: Any,
    val credentialsNonExpired: Boolean,
    val enabled: Boolean,
    val firstName: Any,
    val id: Int,
    val identity: String,
    val lastName: Any,
    val patronymic: Any,
    val username: String,
    val verification: String
)