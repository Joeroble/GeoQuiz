package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG ="QuizViewModel"

class QuizViewModel : ViewModel() {

    // listOf containing various questions and their true/false answers.
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    // variable for the currentIndex that will be used to select a question from the list
    var currentIndex= 0

    // isCheater value used to store the result if the user clicks show answer on the cheat activity.
    var isCheater = false

    val currentQuestionAnswer: Boolean
    get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
    get() = questionBank[currentIndex].textResID

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

}