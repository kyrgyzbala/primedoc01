package kg.kyrgyzcoder.primedoc01.data.aboutUs.repository

import kg.kyrgyzcoder.primedoc01.data.aboutUs.ModelDoc
import kg.kyrgyzcoder.primedoc01.data.aboutUs.model.ModelAboutUs

interface AboutUsRepository {

    suspend fun getAboutUsText(token: String): ModelAboutUs

    suspend fun getContractOffer(token: String): String

    suspend fun getPaidService(token: String): String

    suspend fun getPersonalData(token: String): String

}