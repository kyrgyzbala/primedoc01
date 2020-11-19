package kg.kyrgyzcoder.primedoc01.data.register

data class ModelResetPwd(
    val token: String,
    val password: String,
    val confirmPassword: String
)