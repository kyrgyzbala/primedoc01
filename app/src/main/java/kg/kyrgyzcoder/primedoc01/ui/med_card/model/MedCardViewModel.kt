package kg.kyrgyzcoder.primedoc01.ui.med_card.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelCard
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelUserPhotoResponse
import kg.kyrgyzcoder.primedoc01.data.medcard.repo.MedCardRepository
import okhttp3.MultipartBody
import retrofit2.Response

class MedCardViewModel(private val medCardRepository: MedCardRepository) : ViewModel() {

    suspend fun putUserMedCardData(
        token: String, id: Int, model: ModelCard
    ): Response<ModelCard> {
        return medCardRepository.putUserMedCardData(token, id, model)
    }

    suspend fun putUserPhoto(
        token: String,
        id: Int,
        imageFile: MultipartBody.Part?
    ): Response<ModelUserPhotoResponse> {
        return medCardRepository.putUserPhoto(token, id, imageFile)
    }

    suspend fun getUserPhoto(
        token: String,
        id: Int
    ): ModelUserPhotoResponse {
        return medCardRepository.getUserPhoto(token, id)
    }

    suspend fun getUserMedCard(
        token: String,
        id: Int
    ): ModelCard {
        return medCardRepository.getUserMedCard(token, id)
    }

}