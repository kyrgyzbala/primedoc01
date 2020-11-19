package kg.kyrgyzcoder.primedoc01.ui.about.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kg.kyrgyzcoder.primedoc01.data.aboutUs.repository.AboutUsRepository

class AboutUsViewModelFactory(private val aboutUsRepository: AboutUsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AboutUsViewModel(aboutUsRepository) as T
    }
}