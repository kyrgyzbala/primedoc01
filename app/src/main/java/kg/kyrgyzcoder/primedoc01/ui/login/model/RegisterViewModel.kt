package kg.kyrgyzcoder.primedoc01.ui.login.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc01.data.register.*
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateToken
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateTokenResponse
import kg.kyrgyzcoder.primedoc01.data.register.repository.RegisterRepository
import retrofit2.Response

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    suspend fun registerNewUser(modelRegister: ModelRegister): Response<ModelRegister> {
        return registerRepository.registerNewUser(modelRegister)
    }

    suspend fun verifyPhoneNumber(token: String): Response<ModelRegister> {
        return registerRepository.verifyCode(token)
    }

    suspend fun loginWithPwd(model: ModelLogin): Response<ModelLoginResponse> {
        return registerRepository.loginWithPwd(model)
    }

    suspend fun recoverPwd(phone: String): Response<ModelRecoveryResponse> {
        return registerRepository.recoverPwd(phone)
    }

    suspend fun resetPwd(modelResetPwd: ModelResetPwd): Response<ModelResetResponse> {
        return registerRepository.resetPwd(modelResetPwd)
    }

    suspend fun updateFcmToken(
        token: String,
        model: ModelUpdateToken
    ): Response<ModelUpdateTokenResponse> {
        return registerRepository.updateFcmToken(token, model)
    }

}
