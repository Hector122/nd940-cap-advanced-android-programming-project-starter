package com.example.android.politicalpreparedness.election.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(
        private val database: ElectionDatabase,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
                        ) {
    
    fun getAllElections(): LiveData<List<Election>> {
        return database.electionDao.getAllElections()
    }
    
    suspend fun getElectionById(id: Int): Election? = withContext(ioDispatcher) {
        return@withContext database.electionDao.getElectionById(id)
    }
    
    suspend fun insertElection(election: Election) {
        withContext(ioDispatcher) {
            database.electionDao.insert(election)
        }
    }
    
    suspend fun removeElection(id: Int) {
        withContext(ioDispatcher) {
            database.electionDao.deleteElection(id)
        }
    }
    
    suspend fun clearElections() {
        withContext(ioDispatcher) {
            database.electionDao.clear()
        }
    }

//    TODO:
//    suspend fun refreshElections() {
//        clearElections()
//
//        var elections: List<Election>
//
//        withContext(Dispatchers.IO) {
//            val electionResponse: ElectionResponse = CivicsApi.retrofitService.getElectionsAsync()
//                .await()
//            elections = electionResponse.elections
//
//            elections.forEach { election ->
//                database.electionDao.getElectionById(election.id) { savedElection ->
//                    if (election.id == savedElection.id) {
//                        election.isSaved = savedElection.isSaved
//                    }
//                }
//            }
//            database.electionDao.insertAll(elections)
//        }
//    }

//    suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse? {
//        var voterInfo: VoterInfoResponse?
//        withContext(Dispatchers.IO) {
//            val voterInfoResponse: VoterInfoResponse =
//                CivicsApi.retrofitService.getVoterInfoAsync(electionId, address)
//                    .await()
//            voterInfo = voterInfoResponse
//        }
//        return voterInfo
//    }
}