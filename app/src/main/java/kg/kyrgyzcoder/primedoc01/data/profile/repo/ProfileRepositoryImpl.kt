package kg.kyrgyzcoder.primedoc01.data.profile.repo

import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.profile.model.ModelReservationH

class ProfileRepositoryImpl(private val apiService: ApiService) : ProfileRepository {
    override suspend fun getReservationsDoneAsync(
        token: String,
        userId: Int
    ): List<ModelReservationH> {
        return apiService.getReservationsDoneAsync(token, userId).await()
    }

    override suspend fun getReservationsUpcomingAsync(
        token: String,
        userId: Int
    ): List<ModelReservationH> {
        return apiService.getUpcomingReservationsAsync(token, userId).await()
    }

    override suspend fun getReservationsConfirmedAsync(
        token: String,
        userId: Int
    ): List<ModelReservationH> {
        return apiService.getConfirmedReservationsAsync(token, userId).await()
    }
}