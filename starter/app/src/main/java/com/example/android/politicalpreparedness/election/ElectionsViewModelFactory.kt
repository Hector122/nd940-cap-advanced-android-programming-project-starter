package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

//completed: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(private val  application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)){
            return ElectionsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}