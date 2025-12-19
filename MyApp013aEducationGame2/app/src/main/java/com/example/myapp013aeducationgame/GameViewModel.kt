package com.example.myapp013aeducationgame

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GameViewModel(application: Application, private val playerId: Int) : AndroidViewModel(application) {

    private val questionDao: QuestionDao
    private val gameResultDao: GameResultDao

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> = _currentQuestion

    private val _gameFinished = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> = _gameFinished

    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0

    init {
        val database = AppDatabase.getDatabase(application)
        questionDao = database.questionDao()
        gameResultDao = database.gameResultDao()
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            questions = questionDao.getRandomQuestions(5) // Get 5 random questions
            if (questions.isNotEmpty()) {
                _currentQuestion.value = questions[currentQuestionIndex]
            } else {
                _gameFinished.value = true
            }
        }
    }

    fun onAnswerSelected(answer: String) {
        val question = _currentQuestion.value
        if (question != null && answer == question.correctAnswer) {
            _score.value = (_score.value ?: 0) + 1
        }

        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            _currentQuestion.value = questions[currentQuestionIndex]
        } else {
            finishGame()
        }
    }

    private fun finishGame() {
        viewModelScope.launch {
            gameResultDao.insert(GameResult(playerId = playerId, score = _score.value ?: 0))
            _gameFinished.value = true
        }
    }
}
