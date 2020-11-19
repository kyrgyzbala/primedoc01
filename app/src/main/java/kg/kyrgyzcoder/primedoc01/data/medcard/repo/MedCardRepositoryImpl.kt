package kg.kyrgyzcoder.primedoc01.data.medcard.repo

import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelCard
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelUserPhotoResponse
import okhttp3.MultipartBody
import retrofit2.Response

class MedCardRepositoryImpl(private val apiService: ApiService) : MedCardRepository {


    override suspend fun putUserMedCardData(
        token: String,
        id: Int,
        model: ModelCard
    ): Response<ModelCard> {
        return apiService.putUserMedCardDataAsync(token, id, model).await()
    }

    override suspend fun putUserPhoto(
        token: String,
        id: Int,
        imageFile: MultipartBody.Part?
    ): Response<ModelUserPhotoResponse> {
        return apiService.putUserPhotoAsync(token, id, imageFile).await()
    }

    override suspend fun getUserPhoto(token: String, id: Int): ModelUserPhotoResponse {
        return apiService.getUserPhotoAsync(token, id).await()
    }

    override suspend fun getUserMedCard(token: String, id: Int): ModelCard {
        return apiService.getUserCardAsync(token, id).await()
    }
}