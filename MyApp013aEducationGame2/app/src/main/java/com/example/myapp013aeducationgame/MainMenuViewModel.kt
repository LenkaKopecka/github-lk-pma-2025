package com.example.myapp013aeducationgame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val playerDao: PlayerDao
    val latestPlayer: LiveData<Player?>

    init {
        val database = AppDatabase.getDatabase(application)
        playerDao = database.playerDao()
        latestPlayer = playerDao.getLatestPlayer()
    }

    fun getOrCreatePlayer(playerName: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            var player = playerDao.getPlayerByName(playerName)
            if (player == null) {
                player = Player(name = playerName)
                val playerId = playerDao.insert(player)
                callback.invoke(playerId.toInt())
            } else {
                callback.invoke(player.id)
            }
        }
    }
}
