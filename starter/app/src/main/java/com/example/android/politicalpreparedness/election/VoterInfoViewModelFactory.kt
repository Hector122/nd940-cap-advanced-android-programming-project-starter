package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

//completed: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val application: Application,
                                private val args: VoterInfoFragmentArgs) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(application, args) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}