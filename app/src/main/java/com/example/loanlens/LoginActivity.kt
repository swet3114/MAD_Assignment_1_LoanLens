package com.example.loanlens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginUsername: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Database instance
        database = FirebaseDatabase.getInstance()

        // Initialize UI elements
        loginUsername = findViewById(R.id.login_username)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signupRedirectText)

        // Handle login button click
        loginButton.setOnClickListener {
            if (validateUsername() && validatePassword()) {
                checkUser()
            }
        }

        // Handle signup redirect text click
        signupRedirectText.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateUsername(): Boolean {
        val username = loginUsername.text.toString()
        return if (username.isEmpty()) {
            loginUsername.error = "Username cannot be empty"
            loginUsername.requestFocus()
            false
        } else {
            loginUsername.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = loginPassword.text.toString()
        return if (password.isEmpty()) {
            loginPassword.error = "Password cannot be empty"
            loginPassword.requestFocus()
            false
        } else {
            loginPassword.error = null
            true
        }
    }

    private fun checkUser() {
        val userUsername = loginUsername.text.toString().trim()
        val userPassword = loginPassword.text.toString().trim()

        val reference = database.getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Username found, retrieve password
                    val userSnapshot = snapshot.children.firstOrNull()
                    val passwordFromDB = userSnapshot?.child("password")?.getValue(String::class.java)

                    if (passwordFromDB == userPassword) {
                        // Successful login, retrieve other user details
                        val nameFromDB = userSnapshot.child("name").getValue(String::class.java)
                        val emailFromDB = userSnapshot.child("email").getValue(String::class.java)
                        val usernameFromDB = userSnapshot.child("username").getValue(String::class.java)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                            putExtra("name", nameFromDB)
                            putExtra("email", emailFromDB)
                            putExtra("username", usernameFromDB)
                            putExtra("password", passwordFromDB)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        loginPassword.error = "Invalid Credentials"
                        loginPassword.requestFocus()
                    }
                } else {
                    loginUsername.error = "User does not exist"
                    loginUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
