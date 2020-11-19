package kg.kyrgyzcoder.primedoc01.ui.about.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc01.data.aboutUs.ModelDoc
import kg.kyrgyzcoder.primedoc01.data.aboutUs.model.ModelAboutUs
import kg.kyrgyzcoder.primedoc01.data.aboutUs.repository.AboutUsRepository

class AboutUsViewModel(private val aboutUsRepository: AboutUsRepository) : ViewModel() {

    suspend fun getAboutUs(token: String): ModelAboutUs {
        return aboutUsRepository.getAboutUsText(token)
    }

    suspend fun getContractOffer(token: String): String {
        return aboutUsRepository.getContractOffer(token)
    }

    suspend fun getPaidService(token: String): String {
        return aboutUsRepository.getPaidService(token)
    }

    suspend fun getPersonalData(token: String): String {
        return aboutUsRepository.getPersonalData(token)
    }


}