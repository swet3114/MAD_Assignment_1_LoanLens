package com.example.loanlens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get references to UI elements
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        saveButton = findViewById(R.id.saveButton)

        // Load user data (assuming you have a function to get user data)
        loadUserData()

        // Set click listener for the save button
        saveButton.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun loadUserData() {
        val user: FirebaseUser? = auth.currentUser
        if (user != null) {
            editName.setText(user.displayName)
            editEmail.setText(user.email)
            // Assuming you have stored username in Firestore or Realtime Database
            // Load username from your database and set it
            // editUsername.setText(yourRetrievedUsername)
        }
    }

    private fun saveUserProfile() {
        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val username = editUsername.text.toString().trim()
        val password = editPassword.text.toString().trim()

        // Update user profile
        val user: FirebaseUser? = auth.currentUser
        if (user != null) {
            user.updateProfile(userProfileChangeRequest {
                displayName = name
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            // Update email if changed
            if (email != user.email) {
                user.updateEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Update password if provided
            if (password.isNotEmpty()) {
                user.updatePassword(password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Save username to your database (Firestore or Realtime Database)
            // Implement the database logic to save username here
        }
    }
}
