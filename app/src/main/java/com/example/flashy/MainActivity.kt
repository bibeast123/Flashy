package com.example.flashy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var currentCardDisplayedIndex = 0

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        findViewById<View>(R.id.myNextBtn).setOnClickListener {

            if (allFlashcards.size == 0) {

                return@setOnClickListener
            }
            val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
            val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)


            val leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in)
            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                    flashcardQuestion.visibility = View.VISIBLE
                    flashcardAnswer.visibility = View.INVISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                    findViewById<View>(R.id.flashcard_question).startAnimation(rightInAnim)

                    currentCardDisplayedIndex++


                    if(currentCardDisplayedIndex >= allFlashcards.size) {
                        Snackbar.make(
                            findViewById<TextView>(R.id.flashcard_question),
                            "You've reached the end of the cards, going back to start.",
                            Snackbar.LENGTH_SHORT)
                            .show()
                        currentCardDisplayedIndex = 0
                    }


                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                    val (question, answer) = allFlashcards[currentCardDisplayedIndex]


                    findViewById<TextView>(R.id.flashcard_question).text = question
                    findViewById<TextView>(R.id.flashcard_answer).text = answer
                    flashcardQuestion.visibility = View.VISIBLE
                    flashcardAnswer.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })
            flashcardQuestion.startAnimation(leftOutAnim)
        }
        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        flashcardQuestion.setOnClickListener {
            val answerSideView = findViewById<View>(R.id.flashcard_answer)

// get the center for the clipping circle

// get the center for the clipping circle
            val cx = answerSideView.width / 2
            val cy = answerSideView.height / 2

// get the final radius for the clipping circle

// get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

// create the animator for this view (the start radius is zero)

// create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius)
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
            anim.duration = 1000
            anim.start()
        }
        flashcardAnswer.setOnClickListener {
            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) {
                val Question = data.getStringExtra("Question")
                val Answer = data.getStringExtra("Answer")
                flashcardQuestion.text = Question
                flashcardAnswer.text = Answer

                if (Question != null && Answer != null) {
                    flashcardDatabase.insertCard(Flashcard(Question, Answer))

                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                } else {
                    Log.e("TAG", "Missing question or answer to input into database. Question is $Question and answer is $Answer")
                }
                // Log the value of the strings for easier debugging
                Log.i("MainActivity", "Question: $Question")
                Log.i("MainActivity", "Answer: $Answer")
            } else {
            Log.i("MainActivity", "Returned null data from AddCardActivity")
        }
        }
        findViewById<View>(R.id.myBtn).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)


            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }





    }

}