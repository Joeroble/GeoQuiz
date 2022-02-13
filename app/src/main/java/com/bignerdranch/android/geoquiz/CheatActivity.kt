package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
// const that will be used to pass data between the activities
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"

class CheatActivity : AppCompatActivity() {

    // Pre-initialization for the answerTextView and showAnswerButton
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    // Establishes the answerIsTrue variable that will take in the value for the
    // corresponding questions answer.
    private var answerIsTrue = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        //Establishes value to the question to answerIsTrue, and sets up the text view and button.
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton  = findViewById(R.id.show_answer_button)

        //Sets up the onClickListener for the showAnswerButton, this will check the value in
        // answerIsTrue, save that in answerText, and pass answerText to answerTextView. Finally, it
        // passes true to setAnswerShownResult.
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    // sets up setAnswerShownResult, using Intent().apply, isAnswerShown is tied to EXTRA_ANSWER_SHOWN
    // and the result is set to RESULT_OK.  This is passed back to the main activity.  The commented
    // out code was the book variation, it would not pass back true values or at the very least
    // main activity didn't handle it any meaningful way.
    private fun setAnswerShownResult(isAnswerShown: Boolean){

        Intent().apply{
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            setResult(RESULT_OK,this)
        }
//        val data = Intent().apply{
//            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
//        }
//        setResult(Activity.RESULT_OK, data)
    }

    // sets up a companion object and function newIntent that will return the putExtra back to the main
    // activity with EXTRA_ANSWER_IS_TRUE associated with answerIsTrue
    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}