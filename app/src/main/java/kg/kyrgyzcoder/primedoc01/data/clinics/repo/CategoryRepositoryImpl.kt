package kg.kyrgyzcoder.primedoc01.data.clinics.repo

import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.clinics.model.ModelCategory
import kg.kyrgyzcoder.primedoc01.data.clinics.model.detail.ModelCategDetail
import kg.kyrgyzcoder.primedoc01.data.clinics.model.doctor.ModelDoctorInfo
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.ModelMakeReservation
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response.ModelReservResponse
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.ModelWorkTime
import retrofit2.Response

class CategoryRepositoryImpl(private val apiService: ApiService) : CategoryRepository {

    override suspend fun getCategories(token: String): ModelCategory {
        return apiService.getCategoriesAsync(token).await()
    }

    override suspend fun getCategoryDetail(token: String, id: Int): ModelCategDetail {
        return apiService.getCategoryDetailAsync(token, id).await()
    }

    override suspend fun getDoctorInfo(token: String, id: Int): ModelDoctorInfo {
        return apiService.getDoctorProfileAsync(token, id).await()
    }

    override suspend fun getDoctorWorkTime(token: String, id: Int): ModelWorkTime {
        return apiService.getDoctorWorkTimeAsync(token, id).await()
    }

    override suspend fun makeReservation(
        token: String,
        model: ModelMakeReservation
    ): Response<ModelReservResponse> {
        return apiService.makeReservationAsync(token, model).await()
    }
}