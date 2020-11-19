package kg.kyrgyzcoder.primedoc01.data.aboutUs.repository

import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.aboutUs.ModelDoc
import kg.kyrgyzcoder.primedoc01.data.aboutUs.model.ModelAboutUs

class AboutUsRepositoryImpl(private val apiService: ApiService) : AboutUsRepository {

    override suspend fun getAboutUsText(token: String): ModelAboutUs {
        return apiService.getAboutUsTextAsync(token).await()
    }

    override suspend fun getContractOffer(token: String): String {
        return apiService.getContractOfferAsync(token).await()
    }

    override suspend fun getPaidService(token: String): String {
        return apiService.getPaidServiceAsync(token).await()
    }

    override suspend fun getPersonalData(token: String): String {
        return apiService.getPersonalDataAsync(token).await()
    }
}
