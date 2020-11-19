package kg.kyrgyzcoder.primedoc01.data.register.repository

import kg.kyrgyzcoder.primedoc01.data.register.*
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateToken
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateTokenResponse
import retrofit2.Response

interface RegisterRepository {

    suspend fun registerNewUser(modelRegister: ModelRegister): Response<ModelRegister>
    suspend fun verifyCode(token: String): Response<ModelRegister>

    suspend fun loginWithPwd(model: ModelLogin): Response<ModelLoginResponse>
    suspend fun recoverPwd(phone: String): Response<ModelRecoveryResponse>
    suspend fun resetPwd(modelResetPwd: ModelResetPwd): Response<ModelResetResponse>
    suspend fun updateFcmToken(
        token: String,
        model: ModelUpdateToken
    ): Response<ModelUpdateTokenResponse>
}