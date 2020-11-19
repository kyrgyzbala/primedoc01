package kg.kyrgyzcoder.primedoc01.ui.med_card.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc01.data.profile.model.ModelReservationH
import kg.kyrgyzcoder.primedoc01.data.profile.repo.ProfileRepository

class ProfileViewModel(private val profileRepository: ProfileRepository): ViewModel() {

    suspend fun getReservationsDoneAsync(token: String, userId: Int): List<ModelReservationH> {
        return profileRepository.getReservationsDoneAsync(token, userId)
    }

    suspend fun getReservationsUpcomingAsync(token: String, userId: Int): List<ModelReservationH> {
        return profileRepository.getReservationsUpcomingAsync(token, userId)
    }

    suspend fun getReservationsConfirmedAsync(token: String, userId: Int): List<ModelReservationH> {
        return profileRepository.getReservationsConfirmedAsync(token, userId)
    }

}