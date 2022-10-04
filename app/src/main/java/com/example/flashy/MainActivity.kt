package com.example.flashy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) { // Check that we have data returned
                val Question = data.getStringExtra("Question") // 'string1' needs to match the key we used when we put the string in the Intent
                val Answer = data.getStringExtra("Answer")
                flashcardQuestion.text = Question
                flashcardAnswer.text = Answer
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