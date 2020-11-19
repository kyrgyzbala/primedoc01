package kg.kyrgyzcoder.primedoc01.data.clinics.repo

import kg.kyrgyzcoder.primedoc01.data.clinics.model.ModelCategory
import kg.kyrgyzcoder.primedoc01.data.clinics.model.detail.ModelCategDetail
import kg.kyrgyzcoder.primedoc01.data.clinics.model.doctor.ModelDoctorInfo
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.ModelMakeReservation
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.response.ModelReservResponse
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.ModelWorkTime
import retrofit2.Response

interface CategoryRepository {

    suspend fun getCategories(token: String): ModelCategory

    suspend fun getCategoryDetail(token: String, id: Int): ModelCategDetail

    suspend fun getDoctorInfo(token: String, id: Int): ModelDoctorInfo

    suspend fun getDoctorWorkTime(token: String, id: Int): ModelWorkTime

    suspend fun makeReservation(token: String, model: ModelMakeReservation): Response<ModelReservResponse>
}