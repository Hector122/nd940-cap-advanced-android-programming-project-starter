package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.repository.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.HttpException

class VoterInfoViewModel(application: Application, args: VoterInfoFragmentArgs) :
        AndroidViewModel(application) {
    
    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionRepository(database)
    
    private val division = args.argDivision
    private val electionId = args.argElectionId
    
    //completed Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> get() = _voterInfo
    
    //complete: Add var and methods to support loading URLs
    private val _votingLocationsUrl = MutableLiveData<String?>()
    val votingLocationsUrl: LiveData<String?>
        get() = _votingLocationsUrl
    
    private val _ballotInformationUrl = MutableLiveData<String?>()
    val ballotInformationUrl: LiveData<String?>
        get() = _ballotInformationUrl
    
    private val _isElectionSaved = MutableLiveData<Boolean>()
    val isElectionSaved: LiveData<Boolean>
        get() = _isElectionSaved
    
    //completed: Add var and methods to populate voter info
    init {
        getVoterInfo()
        
        getElectionFromDb()
    }
    
    //https://knowledge.udacity.com/questions/507353
    private fun getVoterInfo() {
        viewModelScope.launch {
            val address = if (division.state.isNotEmpty()) "${division.state} ${division.country}"
            else division.country
            
            try {
                _voterInfo.value = CivicsApi.retrofitService.getVoterInfoAsync(address, electionId)
                    .await()
            } catch (exception:HttpException){
                _voterInfo.value = null
                Log.e("VoterInfoViewModel" , "URL unavailable")
            }
        }
    }
    
    fun onVotingLocationClick() {
        _votingLocationsUrl.value =
            _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }
    
    fun onVotingLocationNavigated() {
        _votingLocationsUrl.value = null
    }
    
    fun onBallotInformationNavigated() {
        _ballotInformationUrl.value = null
    }
    
    fun onBallotInformationClick() {
        _votingLocationsUrl.value =
            _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }
    
    //COMPLETE: Add var and methods to save and remove elections to local database
    //COMPLETE: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    private fun saveElectionToDb() {
        viewModelScope.launch {
            voterInfo.value?.let {
                repository.insertElection(it.election)
            }
            _isElectionSaved.value = true
        }
    }
    
    private fun removeElectionFromDb() {
        viewModelScope.launch {
            voterInfo.value?.let {
                repository.removeElection(it.election.id)
            }
            _isElectionSaved.value = false
        }
    }
    
    fun onFollowElectionClick() {
        when (_isElectionSaved.value) {
            true -> removeElectionFromDb()
            else -> saveElectionToDb()
        }
    }
    
    private fun getElectionFromDb() {
        viewModelScope.launch {
            _isElectionSaved.value = repository.getElectionById(electionId) != null
        }
    }
    
    /**
     * Hint: The saved state can be accomplished in multiple ways.
     * It is directly related to how elections are saved/removed from the database.
     */
}