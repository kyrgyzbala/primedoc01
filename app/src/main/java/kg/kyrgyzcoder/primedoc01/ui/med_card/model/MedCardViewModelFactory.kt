package kg.kyrgyzcoder.primedoc01.ui.med_card.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kg.kyrgyzcoder.primedoc01.data.medcard.repo.MedCardRepository

class MedCardViewModelFactory(private val medCardRepository: MedCardRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MedCardViewModel(medCardRepository) as T
    }
}