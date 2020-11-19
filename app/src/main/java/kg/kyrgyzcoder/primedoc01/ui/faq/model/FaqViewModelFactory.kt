package kg.kyrgyzcoder.primedoc01.ui.faq.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kg.kyrgyzcoder.primedoc01.data.faq.repo.FaqRepository

class FaqViewModelFactory(private val faqRepository: FaqRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FaqViewModel(faqRepository) as T
    }
}