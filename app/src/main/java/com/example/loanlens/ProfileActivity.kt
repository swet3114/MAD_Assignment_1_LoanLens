package com.example.loanlens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var editProfile: Button
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Database instance
        database = FirebaseDatabase.getInstance()

        // Initialize UI elements
        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        profileUsername = findViewById(R.id.profileUsername)
        profilePassword = findViewById(R.id.profilePassword)
        titleName = findViewById(R.id.titleName)
        titleUsername = findViewById(R.id.titleUsername)
        editProfile = findViewById(R.id.editButton)

        // Display user data
        showUserData()

        // Handle edit profile button click
        editProfile.setOnClickListener { passUserData() }
    }

    private fun showUserData() {
        // Get data passed from LoginActivity
        val nameUser = intent.getStringExtra("name")
        val emailUser = intent.getStringExtra("email")
        val usernameUser = intent.getStringExtra("username")
        val passwordUser = intent.getStringExtra("password")

        // Display data in TextViews
        titleName.text = nameUser
        titleUsername.text = usernameUser
        profileName.text = nameUser
        profileEmail.text = emailUser
        profileUsername.text = usernameUser
        profilePassword.text = passwordUser
    }

    private fun passUserData() {
        val userUsername = profileUsername.text.toString().trim()

        val reference = database.getReference("users")
        val checkUserDatabase = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Retrieve user data from Firebase
                    val userSnapshot = snapshot.children.firstOrNull()
                    val nameFromDB = userSnapshot?.child("name")?.getValue(String::class.java)
                    val emailFromDB = userSnapshot?.child("email")?.getValue(String::class.java)
                    val usernameFromDB = userSnapshot?.child("username")?.getValue(String::class.java)
                    val passwordFromDB = userSnapshot?.child("password")?.getValue(String::class.java)

                    // Pass data to EditProfileActivity
                    val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java).apply {
                        putExtra("name", nameFromDB)
                        putExtra("email", emailFromDB)
                        putExtra("username", usernameFromDB)
                        putExtra("password", passwordFromDB)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this@ProfileActivity, "User does not exist in the database", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Database Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
