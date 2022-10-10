package com.example.flashy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

            val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
            val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
            findViewById<TextView>(R.id.flashcard_question).text = question
            findViewById<TextView>(R.id.flashcard_answer).text = answer
            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
        }
        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
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
        }





    }

}