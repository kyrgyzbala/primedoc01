package kg.kyrgyzcoder.primedoc01.data.register


import com.google.gson.annotations.SerializedName

data class ModelRegister(
    val authorities: List<String>,
    val password: String,
    val username: String
)