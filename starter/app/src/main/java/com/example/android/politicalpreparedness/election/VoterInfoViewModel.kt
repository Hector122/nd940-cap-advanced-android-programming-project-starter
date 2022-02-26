package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.repository.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(application: Application, args: VoterInfoFragmentArgs) :
        AndroidViewModel(application) {
    
    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionRepository(database)
    
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
    
    //TODO: Add var and methods to populate voter info
    // saved election info from database
    private val division = args.argDivision
    private val electionId = args.argElectionId
    
    init {
        getVoterInfo()
        
        getElectionFromDb()
    }
    
    //https://knowledge.udacity.com/questions/507353
    private fun getVoterInfo() {
        viewModelScope.launch {
            var address = "country:${division.country}"
            // state can be sometimes missing from the division retrieved
            // from the electionQuery API call,
            // but have to add some state to the voterinfo API call or it will be rejected
//            address += if (division.state.isNotBlank() && division.state.isNotEmpty()) {
//                "/state:${division.state}"
//            } else {
//                "/state:ca"
//            }
            
            
            address = if (division.state.isNotEmpty()) "${division.state} ${division.country}"
            else division.country
            
            _voterInfo.value = CivicsApi.retrofitService.getVoterInfoAsync(address, electionId)
                .await()
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
    
    
    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    
    /**
     * Hint: The saved state can be accomplished in multiple ways.
     * It is directly related to how elections are saved/removed from the database.
     */
}