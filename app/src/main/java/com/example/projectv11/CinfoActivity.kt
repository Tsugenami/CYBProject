package com.example.projectv11

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

// ConnectionInfo activity for managing and saving connection details
class ConnectionInfo : AppCompatActivity() {

    private lateinit var ipAddressEditText: EditText
    private lateinit var portEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.cinfo)

        // Initialize the views
        ipAddressEditText = findViewById(R.id.ipInput)
        portEditText = findViewById(R.id.portNum)
        usernameEditText = findViewById(R.id.user)
        passwordEditText = findViewById(R.id.pass)
        saveButton = findViewById(R.id.save)

        // Load existing connection info if available
        loadConnectionInfo()

        // Set click listener for the save button
        saveButton.setOnClickListener {
            saveConnectionInfo()
        }

        // Button to navigate back to the MainActivity
        val homeButton = findViewById<Button>(R.id.home)
        homeButton.setOnClickListener{
            val intent4 = Intent(this, MainActivity::class.java)
            startActivity(intent4)
        }
    }

    private fun saveConnectionInfo() {
        // Save connection info to shared preferences
        val preferences: SharedPreferences =
            getSharedPreferences("ConnectionInfo", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("ip_address", ipAddressEditText.text.toString())
        editor.putString("port", portEditText.text.toString())
        editor.putString("username", usernameEditText.text.toString())
        editor.putString("password", passwordEditText.text.toString())
        editor.apply()
    }

    private fun loadConnectionInfo() {
        // Load connection info from shared preferences
        val preferences: SharedPreferences =
            getSharedPreferences("ConnectionInfo", Context.MODE_PRIVATE)
        ipAddressEditText.setText(preferences.getString("ip_address", ""))
        portEditText.setText(preferences.getString("port", ""))
        usernameEditText.setText(preferences.getString("username", ""))
        passwordEditText.setText(preferences.getString("password", ""))
    }
}
