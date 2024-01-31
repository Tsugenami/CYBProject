package com.example.projectv11

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ConnectionInfo : AppCompatActivity() {

    private lateinit var ipAddressEditText: EditText
    private lateinit var portEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cinfo)

       //Initialize the views
        ipAddressEditText = findViewById<EditText>(R.id.ipInput)
        portEditText = findViewById<EditText>(R.id.portNum)
       usernameEditText = findViewById<EditText>(R.id.user)
       passwordEditText = findViewById<EditText>(R.id.pass)
       saveButton = findViewById<Button>(R.id.save)

       //load existing connection info if available
       loadConnectionInfo()

       //set clicklistener for save button
       saveButton.setOnClickListener {
           saveConnectionInfo()
       }






        val hme = findViewById<Button>(R.id.home)
        hme.setOnClickListener{
            val intent4 = Intent(this, MainActivity::class.java)
            startActivity(intent4)
        }




    }

    private fun saveConnectionInfo() {
        //save connection into to shared preferences
        val preferences: SharedPreferences =
            getSharedPreferences("ConnectionInfo", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("ip_address", ipAddressEditText.text.toString())
        editor.putString("port",portEditText.text.toString())
        editor.putString("username", usernameEditText.text.toString())
        editor.putString("password", passwordEditText.text.toString())
        editor.apply()
    }

    private fun loadConnectionInfo(){
        //Load Connection Info from shared preferences
        val preferences: SharedPreferences =
            getSharedPreferences("ConnectionInfo", Context.MODE_PRIVATE)
        ipAddressEditText.setText(preferences.getString("ip_address", ""))
        portEditText.setText(preferences.getString("port",""))
        usernameEditText.setText(preferences.getString("username", ""))
        passwordEditText.setText(preferences.getString("password", ""))
    }
}