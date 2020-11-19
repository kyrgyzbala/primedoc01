package kg.kyrgyzcoder.primedoc01.ui.clinic.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kg.kyrgyzcoder.primedoc01.data.clinics.repo.CategoryRepository

class CategoryViewModelFactory(private val categoryRepository: CategoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryViewModel(categoryRepository) as T
    }
}