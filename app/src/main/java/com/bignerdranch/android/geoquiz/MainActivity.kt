package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    // Sets up the buttons and textview variables, they are not currently initialized but will be
    // later on in the code.
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    // listOf containing various questions and their true/false answers.
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    // variable for the currentIndex that will be used to select a question from the list
    private var currentIndex= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // establishes the buttons and textviews by linking them with the corresponding entities
        // in activity_main.xml
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        // sets up the onclick listener for trueButton that call upon checkAnswer and pass it
        // true in order to check the answer that corresponds to the question(key)
        trueButton.setOnClickListener {view: View ->
            checkAnswer(true)
        }

        // sets up falseButton that will call upon checkAnswer and pass it false to check
        // against the question's answer
        falseButton.setOnClickListener{view: View ->
            checkAnswer(false)
        }

        // sets up nextButton that will move to the next question, increases the currentIndex, and
        // calls upon updateQuestion.
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
           updateQuestion()
        }
        //Updates the question for the initial load.
        updateQuestion()
    }
    // Creates the updateQuestion function where using currentIndex to get a question from the questionBank
    // and storing it in questionTextResID, which then sets the questionTextView to the string stored
    // in questionTextResId
    private fun updateQuestion(){
        val questionTextResId = questionBank[currentIndex].textResID
        questionTextView.setText(questionTextResId)
    }

    // creates the function checkAnswer that will check the answer the user selected against the
    // answer associated with the question in questionBank.  If the userAnswer equals the correctAnswer
    // variable, it will set messageResId to correct or incorrect based on the result of the if/else
    // statement.  It will then be used in the Toast to display to the user.
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer){
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}