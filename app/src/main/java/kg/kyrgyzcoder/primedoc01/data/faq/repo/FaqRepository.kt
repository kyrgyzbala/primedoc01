package kg.kyrgyzcoder.primedoc01.data.faq.repo

import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaq
import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaqItem

interface FaqRepository {

    suspend fun getFaqList(token: String): List<ModelFaqItem>
}