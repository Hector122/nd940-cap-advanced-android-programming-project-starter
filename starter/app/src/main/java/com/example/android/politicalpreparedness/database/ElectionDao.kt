package com.example.android.politicalpreparedness.database

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
    @Query("SELECT * FROM election_table")
    suspend fun getAllElections(): List<Election>
    
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