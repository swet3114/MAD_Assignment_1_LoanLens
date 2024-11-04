package com.example.loanlens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var signupName: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize UI elements
        signupName = findViewById(R.id.signup_name)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")

        signupButton.setOnClickListener {
            // Get the entered user data
            val name = signupName.text.toString().trim()
            val email = signupEmail.text.toString().trim()
            val username = signupUsername.text.toString().trim()
            val password = signupPassword.text.toString().trim()

            if (validateInputs(name, email, username, password)) {
                // Create a user helper class instance with the user data
                val helperClass = HelperClass(name, email, username, password)

                // Store data in Firebase under the "username" node
                reference.child(username).setValue(helperClass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()  // Close SignupActivity after successful signup
                    } else {
                        Toast.makeText(this, "Signup failed. Please try again.", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { e ->
                    // Show detailed error message if signup fails
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        loginRedirectText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()  // Close SignupActivity when redirecting to LoginActivity
        }
    }

    private fun validateInputs(name: String, email: String, username: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                signupName.error = "Name cannot be empty"
                signupName.requestFocus()
                false
            }
            email.isEmpty() -> {
                signupEmail.error = "Email cannot be empty"
                signupEmail.requestFocus()
                false
            }
            username.isEmpty() -> {
                signupUsername.error = "Username cannot be empty"
                signupUsername.requestFocus()
                false
            }
            password.isEmpty() -> {
                signupPassword.error = "Password cannot be empty"
                signupPassword.requestFocus()
                false
            }
            password.length < 6 -> {
                signupPassword.error = "Password should be at least 6 characters"
                signupPassword.requestFocus()
                false
            }
            else -> true
        }
    }
}
