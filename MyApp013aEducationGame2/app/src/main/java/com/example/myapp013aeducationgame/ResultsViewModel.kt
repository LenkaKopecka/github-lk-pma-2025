package com.example.myapp013aeducationgame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ResultsViewModel(application: Application) : AndroidViewModel(application) {

    private val gameResultDao: GameResultDao
    val allResults: LiveData<List<GameResult>>

    init {
        val database = AppDatabase.getDatabase(application)
        gameResultDao = database.gameResultDao()
        allResults = gameResultDao.getAllResults()
    }
}
