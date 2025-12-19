package com.example.myapp013aeducationgame

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameResult: GameResult)

    @Query("SELECT * FROM game_results ORDER BY score DESC")
    fun getAllResults(): LiveData<List<GameResult>>
}
