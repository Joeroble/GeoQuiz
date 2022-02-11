package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val CHEAT_VALUE = "cheater"
private const val REQUEST_CODE_CHEAT = 0



class MainActivity : AppCompatActivity() {

    // Sets up the buttons and textview variables, they are not currently initialized but will be
    // later on in the code.
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        // establishes the buttons and textviews by linking them with the corresponding entities
        // in activity_main.xml
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
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
            quizViewModel.moveToNext()
           updateQuestion()
        }

        cheatButton.setOnClickListener{
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)

        }


        //Updates the question for the initial load.
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putBoolean(CHEAT_VALUE, quizViewModel.isCheater)

    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }



    // Creates the updateQuestion function where using currentIndex to get a question from the questionBank
    // and storing it in questionTextResID, which then sets the questionTextView to the string stored
    // in questionTextResId
    private fun updateQuestion(){
        val  questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    // creates the function checkAnswer that will check the answer the user selected against the
    // answer associated with the question in questionBank.  If the userAnswer equals the correctAnswer
    // variable, it will set messageResId to correct or incorrect based on the result of the if/else
    // statement.  It will then be used in the Toast to display to the user.
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}