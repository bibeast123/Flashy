package com.example.flashy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        val question=findViewById<EditText>(R.id.QuestionField);
        val answer =findViewById<EditText>(R.id.AnswerField);

        findViewById<View>(R.id.myCancelBtn).setOnClickListener {
            finish()
        }
        findViewById<View>(R.id.mySaveButton).setOnClickListener {
            val quesString= question.text.toString();
            val ansString=answer.text.toString();
            val data = Intent() // create a new Intent, this is where we will put our data

            data.putExtra(
                "Question",
                quesString
            ) // puts one string into the Intent, with the key as 'string1'

            data.putExtra(
                "Answer",
                ansString
            ) // puts another string into the Intent, with the key as 'string2

            setResult(RESULT_OK, data) // set result code and bundle data for response

            finish()
        }


    }
}