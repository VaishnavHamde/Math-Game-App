package com.example.mathgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    lateinit var textScore : TextView
    lateinit var textLife : TextView
    lateinit var textTime : TextView
    lateinit var textQuestion : TextView
    lateinit var editAnswer : EditText
    lateinit var buttonOkay : Button
    lateinit var buttonNext : Button

    lateinit var timer : CountDownTimer
    private val startTimerInMillis : Long = 40000
    var timeLeftInMillis : Long = 40000

    var correctAnswer = 0
    var userScore = 0
    var userLife = 3

    var category : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        if(intent.getIntExtra("addition", 0) != 0)
            category = 1
        else if(intent.getIntExtra("subtraction", 0) != 0)
            category = 2
        else
            category = 3

        supportActionBar!!.title = "Addition Game"

        textScore = findViewById(R.id.textview_score)
        textLife = findViewById(R.id.textview_life)
        textTime = findViewById(R.id.textview_time)
        textQuestion = findViewById(R.id.textview_question)
        editAnswer = findViewById(R.id.edittext_answer)
        buttonOkay = findViewById(R.id.button_okay)
        buttonNext = findViewById(R.id.button_next)

        gameContinue()

        buttonOkay.setOnClickListener {

            val input = editAnswer.text.toString()

            if(input == "")
                Toast.makeText(this@GameActivity, "Please write an answer or " +
                        "click on the next button", Toast.LENGTH_SHORT).show()
            else{
                pauseTimer()

                val userAnswer = input.toInt()

                if(userAnswer == correctAnswer){
                    userScore += 10
                    textQuestion.text = "Congratulations, your answer is correct!"
                    textScore.text = userScore.toString()
                }
                else{
                    userLife--
                    textQuestion.text = "Sorry your answer is wrong :("
                    textLife.text = userLife.toString()
                }
            }
        }

        buttonNext.setOnClickListener {
            pauseTimer()
            resetTimer()

            editAnswer.setText("")


            if(userLife == 0){
                Toast.makeText(this@GameActivity, "Game Over", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(this@GameActivity, ResultActivity::class.java)
                intent.putExtra("score", userScore)
                startActivity(intent)
                finish()
            }
            else
                gameContinue()
        }
    }

    fun gameContinue(){
        var number1 = Random.nextInt(1,100)
        var number2 = Random.nextInt(1,100)

        if(category == 1) {
            textQuestion.text = "$number1 + $number2"
            correctAnswer = number1 + number2
        }
        else if(category == 2){
            while(number1 < number2){
                number1 = Random.nextInt(1,100)
                number2 = Random.nextInt(1,100)
            }
            textQuestion.text = "$number1 - $number2"
            correctAnswer = number1 - number2
        }
        else{
            textQuestion.text = "$number1 * $number2"
            correctAnswer = number1 * number2
        }

        resetTimer()
        startTimer()
    }

    fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000){

            override fun onTick(millisUntilFinished : Long) {
                timeLeftInMillis = millisUntilFinished
                updateText()
            }

            override fun onFinish() {
                pauseTimer()

                userLife--

                textLife.text = userLife.toString()
                textQuestion.text = "Sorry, time is up :("
            }

        }.start()
    }

    fun updateText(){
        val remainingTime : Int = (timeLeftInMillis/1000).toInt()
        textTime.text = String.format(Locale.getDefault(), "%02d", remainingTime)
    }

    fun pauseTimer(){
        timer.cancel()
    }

    fun resetTimer(){
        timeLeftInMillis = startTimerInMillis
        updateText()
    }
}