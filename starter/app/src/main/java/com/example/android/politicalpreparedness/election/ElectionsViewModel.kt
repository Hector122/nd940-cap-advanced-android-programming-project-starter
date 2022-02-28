package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.repository.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.launch

//completed: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionRepository(database)
    
    //completed: Create live data val for upcoming elections
    private val _upcomingElection = MutableLiveData<List<Election>>()
    val upcomingElection: LiveData<List<Election>> get() = _upcomingElection
    
    //completed: Create live data val for saved elections
    // private val _savedElections = MutableLiveData<List<Election>>()
    val savedElection: LiveData<List<Election>> get() = repository.getAllElections()
    
    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo: LiveData<Election> get() = _navigateToVoterInfo
    
    //completed: Create val and functions to populate live data
    // for upcoming elections from the API and saved elections from local database
    init {
        getUpcomingElections()
    }
    
    private fun getSavedElection() {
        viewModelScope.launch {
            // _savedElections.value = database.electionDao.getAllElections().value
        }
    }
    
    private fun getUpcomingElections() {
        viewModelScope.launch {
            try {
                val electionResponse: ElectionResponse = CivicsApi.retrofitService.getElectionsAsync().await()
                _upcomingElection.value = electionResponse.elections
            } catch (exception: Exception) {
                Log.e("getUpcomingElections", "Exception getting upcoming elections")
            }
        }
    }
    
    //completed: Create functions to navigate to saved or upcoming election voter info
    fun navigateToVoterInfo(election: Election) {
        _navigateToVoterInfo.value = election
    }
    
    fun onVoterInfoNavigated() {
        _navigateToVoterInfo.value = null
    }
}