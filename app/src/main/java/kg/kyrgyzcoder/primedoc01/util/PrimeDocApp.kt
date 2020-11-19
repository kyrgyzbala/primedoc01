package kg.kyrgyzcoder.primedoc01.util

import android.app.Application
import kg.kyrgyzcoder.primedoc01.data.ApiService
import kg.kyrgyzcoder.primedoc01.data.ConnectivityInterceptorImpl
import kg.kyrgyzcoder.primedoc01.data.aboutUs.repository.AboutUsRepository
import kg.kyrgyzcoder.primedoc01.data.aboutUs.repository.AboutUsRepositoryImpl
import kg.kyrgyzcoder.primedoc01.data.clinics.repo.CategoryRepository
import kg.kyrgyzcoder.primedoc01.data.clinics.repo.CategoryRepositoryImpl
import kg.kyrgyzcoder.primedoc01.data.faq.repo.FaqRepository
import kg.kyrgyzcoder.primedoc01.data.faq.repo.FaqRepositoryImpl
import kg.kyrgyzcoder.primedoc01.data.medcard.repo.MedCardRepository
import kg.kyrgyzcoder.primedoc01.data.medcard.repo.MedCardRepositoryImpl
import kg.kyrgyzcoder.primedoc01.data.payment.repo.PaymentRepository
import kg.kyrgyzcoder.primedoc01.data.payment.repo.PaymentRepositoryImpl
import kg.kyrgyzcoder.primedoc01.data.profile.repo.ProfileRepository
import kg.kyrgyzcoder.primedoc01.data.profile.repo.ProfileRepositoryImpl
import kg.kyrgyzcoder.primedoc01.data.register.repository.RegisterRepository
import kg.kyrgyzcoder.primedoc01.data.register.repository.RegisterRepositoryImpl
import kg.kyrgyzcoder.primedoc01.ui.about.model.AboutUsViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.PaymentViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.faq.model.FaqViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.MedCardViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.ProfileViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


/**
 *  PrimeDocApp that extends Application and implements KodeinAware Dependency Injection
 */

class PrimeDocApp : Application(), KodeinAware {


    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@PrimeDocApp))

        bind<ConnectivityInterceptorImpl>() with singleton {
            ConnectivityInterceptorImpl(instance())
        }
        bind() from singleton { ApiService(instance()) }

        bind<RegisterRepository>() with singleton { RegisterRepositoryImpl(instance()) }
        bind() from provider { RegisterViewModelFactory(instance()) }

        bind<CategoryRepository>() with singleton { CategoryRepositoryImpl(instance()) }
        bind() from provider { CategoryViewModelFactory(instance()) }

        bind<FaqRepository>() with singleton { FaqRepositoryImpl(instance()) }
        bind() from provider { FaqViewModelFactory(instance()) }

        bind<AboutUsRepository>() with singleton { AboutUsRepositoryImpl(instance()) }
        bind() from provider { AboutUsViewModelFactory(instance()) }

        bind<MedCardRepository>() with singleton { MedCardRepositoryImpl(instance()) }
        bind() from provider { MedCardViewModelFactory(instance()) }

        bind<PaymentRepository>() with singleton { PaymentRepositoryImpl(instance()) }
        bind() from provider { PaymentViewModelFactory(instance()) }

        bind<ProfileRepository>() with singleton { ProfileRepositoryImpl(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
    }
}