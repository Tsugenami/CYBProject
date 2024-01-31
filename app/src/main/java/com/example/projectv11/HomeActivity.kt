package com.example.projectv11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

// HomeActivity class responsible for handling the main screen after login
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Note: Each button click listener starts a new activity

        // Button to navigate to the ConnectionInfo activity
        val cinfoButton = findViewById<Button>(R.id.Cinfo)
        cinfoButton.setOnClickListener{
            val intent1 = Intent(this, ConnectionInfo::class.java)
            startActivity(intent1)
        }

        // Button to navigate to the PhoneActivity
        val phstButton = findViewById<Button>(R.id.Phone)
        phstButton.setOnClickListener{
            val intent2 =  Intent(this, PhoneActivity::class.java)
            startActivity(intent2)
        }

        // Button to navigate to the ServerActivity
        val svwButton = findViewById<Button>(R.id.Server)
        svwButton.setOnClickListener{
            val intent3 = Intent(this, ServerActivity::class.java)
            startActivity(intent3)
        }
    }
}
