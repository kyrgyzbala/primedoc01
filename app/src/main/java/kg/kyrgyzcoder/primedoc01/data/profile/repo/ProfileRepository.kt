package kg.kyrgyzcoder.primedoc01.data.profile.repo

import kg.kyrgyzcoder.primedoc01.data.profile.model.ModelReservationH

interface ProfileRepository {


    suspend fun getReservationsDoneAsync(token: String, userId: Int): List<ModelReservationH>
    suspend fun getReservationsUpcomingAsync(token: String, userId: Int): List<ModelReservationH>
    suspend fun getReservationsConfirmedAsync(token: String, userId: Int): List<ModelReservationH>

}