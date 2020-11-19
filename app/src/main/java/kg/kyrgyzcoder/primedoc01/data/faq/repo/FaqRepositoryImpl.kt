package kg.kyrgyzcoder.primedoc01.data.faq.repo

import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaq
import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaqItem

class FaqRepositoryImpl(private val apiService: ApiService) : FaqRepository {


    override suspend fun getFaqList(token: String): List<ModelFaqItem> {
        return apiService.getFaqListAsync(token).await()
    }

}