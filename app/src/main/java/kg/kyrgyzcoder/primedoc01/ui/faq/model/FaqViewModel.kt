package kg.kyrgyzcoder.primedoc01.ui.faq.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaq
import kg.kyrgyzcoder.primedoc01.data.faq.ModelFaqItem
import kg.kyrgyzcoder.primedoc01.data.faq.repo.FaqRepository

class FaqViewModel(private val faqRepository: FaqRepository) : ViewModel() {

    suspend fun getFaqList(token: String): List<ModelFaqItem> {
        return faqRepository.getFaqList(token)
    }

}