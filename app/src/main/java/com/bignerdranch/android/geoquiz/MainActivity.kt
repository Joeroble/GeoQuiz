package com.bignerdranch.android.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0



class MainActivity : AppCompatActivity() {

    //Sets up the cheatResultLauncher that will be used to activate the cheat activity, it uses a lambda function
    // to store the result and passes it to handleCheatResult
    private val cheatResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> handleCheatResult(result)
    }

    // Sets up the buttons and textview variables, they are not currently initialized but will be
    // later on in the code.
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    //sets up the quizViewModel to be accessed by main activity.
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    //This suppresses the android warning about this running on lower hardware then what
    // newer code requires as it won't run on said older software with a check.
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        //This pulls in the value associated with KEY_INDEX to save the current place in the
        // question list, and sets it in currentIndex.
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

        //Sets up the cheatButton - the current version calls on showCheatAnswer to handle
        // everything.  The book code sets up answerIsTrue, and an intent with answerIsTrue.
        // It then checks what version of android is running, if its marshmellow or newer it will
        // run with the makeClipRevealAnimation, which will add in a transition animation,
        // otherwise it will just pass on intent, and request_code_cheat to startActivityForResult.
        cheatButton.setOnClickListener{
            // Start CheatActivity
            showCheatAnswer()
//            val answerIsTrue = quizViewModel.currentQuestionAnswer
//            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                // note, view did not work as written in the book
//                val options = ActivityOptions.makeClipRevealAnimation(it, 0, 0, it.width, it.height)
//                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
//            } else {
//                startActivityForResult(intent, REQUEST_CODE_CHEAT)
//            }


        }


        //Updates the question for the initial load.
        updateQuestion()
    }

    // These commented out sections are from the book and Clara, neither worked - the program would
    // never hit this code, went over it with the debugger.  What is supposed to happen is the data
    // passed back from cheat activity would be checked - if the resultCode was not result_ok it would
    // just move on, otherwise it would take the value associated with EXTRA_ANSWER_SHOWN and store it in
    // data, though it doesn't actually save it in a value, so I'm not sure what the book was trying to accomplish there.
    // Clara's is similar but it just checks the requestCode and not asking for REQUEST_CODE_CHEAT, if it
    // is not RESULT_OK, isCheater is set to false, otherwise the variable cheated takes the data associated
    // to EXTRA_ANSWER_SHOWN and stores it.  Which is then used to set the contents of isCheater.

//    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode != Activity.RESULT_OK){
//            return
//        }
//        if(requestCode == REQUEST_CODE_CHEAT){
//            data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode != Activity.RESULT_OK) {
//            quizViewModel.isCheater = false
//            return
//        } else {
//            val cheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
//            quizViewModel.isCheater = cheated
//
//        }
//    }



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
 //       savedInstanceState.putBoolean(CHEAT_VALUE, quizViewModel.isCheater)

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

    // Judgment toast is added that will activate if isCheater is true.
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    // This will package up the current question's answer, associating it with EXTRA_ANSWER_IS_TRUE,
    // and then calling on cheatResultLauncher to launch the cheat activity.
    private fun showCheatAnswer() {
        Intent(this, CheatActivity::class.java).apply{
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            cheatResultLauncher.launch(this)
        }
    }

    // This handles the result package sent back from cheat activity, it checks if resultCode is RESULT_OK
    // pulls out the data and saves it in intent, and then pulls out the boolean associated with EXTRA_ANSWER_SHOWN
    // and stores it in cheated, which is then used to set the value in isCheater.
    private fun handleCheatResult(result: ActivityResult){
        if(result.resultCode == RESULT_OK){
            val intent = result.data
            val cheated = intent?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.isCheater = cheated
        }
    }
}
