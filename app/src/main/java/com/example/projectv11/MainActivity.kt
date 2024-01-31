package com.example.projectv11

import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Define the main activity class, which extends AppCompatActivity
class MainActivity : AppCompatActivity() {

    // Declare UI elements
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var welcomeText: TextView

    // Retrofit instance for network requests
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-api-base-url/") // Replace with your actual API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Service interface for authentication requests
    private val authService: AuthService = retrofit.create(AuthService::class.java)

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        welcomeText = findViewById(R.id.wilkommen)
    }

    // Function to handle the login button click event
    fun login(view: android.view.View) {
        // Retrieve entered username and password from input fields
        val enteredUsername = usernameInput.text.toString()
        val enteredPassword = passwordInput.text.toString()

        // Create a login request object
        val loginRequest = LoginRequest(enteredUsername, enteredPassword)

        // Send a login request to the server using Retrofit
        authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    // Handle successful server response
                    val loginResponse = response.body()

                    if (loginResponse?.success == true) {
                        // Login successful
                        welcomeText.text = "Login Successful, please continue."

                        // Retrieve session token from the response
                        val sessionToken = loginResponse.sessionToken ?: ""

                        // Delay for usability before starting a new activity
                        Handler().postDelayed({
                            // Start HomeActivity
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 750)
                    } else {
                        // Login unsuccessful
                        welcomeText.text = "Login Unsuccessful, please try again."
                    }
                } else {
                    // Handle server response error
                    welcomeText.text = "Server error, please try again."
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network error
                welcomeText.text = "Network error, please try again."
            }
        })
    }
}
