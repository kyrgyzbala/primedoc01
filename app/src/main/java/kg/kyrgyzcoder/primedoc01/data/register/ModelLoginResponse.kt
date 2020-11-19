package kg.kyrgyzcoder.primedoc01.data.register


import com.google.gson.annotations.SerializedName

data class ModelLoginResponse(
    val accessToken: String,
    val chatToken: String,
    val id: Int,
    val userId: Int,
    val refreshExpirationTime: String,
    val refreshToken: String,
    val tokenExpirationTime: String
)