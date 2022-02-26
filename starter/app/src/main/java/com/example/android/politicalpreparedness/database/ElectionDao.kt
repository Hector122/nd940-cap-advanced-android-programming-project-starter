package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(elections: List<Election>)
    
    //completed: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)
    
    //completed: Add select all election query
    //https://stackoverflow.com/a/57806372
    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>
    
    //completed: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :electionId")
    suspend fun getElectionById(electionId: Int): Election?
    
    //completed: Add delete query
    @Query("DELETE FROM election_table WHERE id = :electionId")
    suspend fun deleteElection(electionId: Int)
    
    //complete: Add clear query
    @Query("DELETE FROM election_table")
    suspend fun clear()
}