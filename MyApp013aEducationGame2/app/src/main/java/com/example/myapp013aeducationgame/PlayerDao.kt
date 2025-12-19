package com.example.myapp013aeducationgame

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: Player): Long

    @Query("SELECT * FROM players WHERE name = :name")
    suspend fun getPlayerByName(name: String): Player?

    @Query("SELECT * FROM players ORDER BY id DESC LIMIT 1")
    fun getLatestPlayer(): LiveData<Player?>
}
