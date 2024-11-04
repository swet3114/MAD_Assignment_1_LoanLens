package com.example.loanlens

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Retrieve and display prediction result
        val predictionResult = intent.getStringExtra("PREDICTION_RESULT")
        val resultTextView: TextView = findViewById(R.id.result_text_view)
        resultTextView.text = predictionResult ?: "No Result Available"

        // Set up back button functionality
        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
    }
}
