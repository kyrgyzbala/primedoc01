package kg.kyrgyzcoder.primedoc01.ui.clinic.model

import androidx.lifecycle.ViewModel
import kg.kyrgyzcoder.primedoc01.data.clinics.model.ModelCategory
import kg.kyrgyzcoder.primedoc01.data.clinics.model.detail.ModelCategDetail
import kg.kyrgyzcoder.primedoc01.data.clinics.model.doctor.ModelDoctorInfo
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.ModelMakeReservation
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response.ModelReservResponse
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.ModelWorkTime
import kg.kyrgyzcoder.primedoc01.data.clinics.repo.CategoryRepository
import retrofit2.Response

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    suspend fun getCategories(token: String): ModelCategory {
        return categoryRepository.getCategories(token)
    }

    suspend fun getCategoryDetail(token: String, id: Int): ModelCategDetail {
        return categoryRepository.getCategoryDetail(token, id)
    }

    suspend fun getDoctorInfo(token: String, id: Int): ModelDoctorInfo {
        return categoryRepository.getDoctorInfo(token, id)
    }

    suspend fun getDoctorWorkTime(token: String, id: Int): ModelWorkTime {
        return categoryRepository.getDoctorWorkTime(token, id)
    }

    suspend fun makeReservation(
        token: String,
        model: ModelMakeReservation
    ): Response<ModelReservResponse> {
        return categoryRepository.makeReservation(token, model)
    }

}