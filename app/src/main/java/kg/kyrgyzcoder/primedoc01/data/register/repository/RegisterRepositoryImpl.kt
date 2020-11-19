package kg.kyrgyzcoder.primedoc01.data.register.repository

import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.register.*
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateToken
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateTokenResponse
import retrofit2.Response

class RegisterRepositoryImpl(private val apiService: ApiService) : RegisterRepository {

    override suspend fun registerNewUser(modelRegister: ModelRegister): Response<ModelRegister> {
        return apiService.registerAsync(modelRegister).await()
    }

    override suspend fun verifyCode(token: String): Response<ModelRegister> {
        return apiService.verifyCodeAsync(token).await()
    }

    override suspend fun loginWithPwd(model: ModelLogin): Response<ModelLoginResponse> {
        return apiService.loginWithPwdAsync(model).await()
    }

    override suspend fun recoverPwd(phone: String): Response<ModelRecoveryResponse> {
        return apiService.recoverPwdAsync(phone).await()
    }

    override suspend fun resetPwd(modelResetPwd: ModelResetPwd): Response<ModelResetResponse> {
        return apiService.resetPwdAsync(modelResetPwd).await()
    }

    override suspend fun updateFcmToken(
        token: String,
        model: ModelUpdateToken
    ): Response<ModelUpdateTokenResponse> {
        return apiService.sendFcmTokenAsync(token, model).await()
    }
}