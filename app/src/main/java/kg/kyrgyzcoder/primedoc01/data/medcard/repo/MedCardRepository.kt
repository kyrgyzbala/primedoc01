package kg.kyrgyzcoder.primedoc01.data.medcard.repo

import kg.kyrgyzcoder.primedoc01.data.medcard.ModelCard
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelUserPhotoResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface MedCardRepository {

    suspend fun putUserMedCardData(
        token: String, id: Int, model: ModelCard
    ): Response<ModelCard>

    suspend fun putUserPhoto(
        token: String,
        id: Int,
        imageFile: MultipartBody.Part?
    ): Response<ModelUserPhotoResponse>

    suspend fun getUserPhoto(
        token: String,
        id: Int
    ): ModelUserPhotoResponse

    suspend fun getUserMedCard(
        token: String,
        id: Int
    ): ModelCard

}