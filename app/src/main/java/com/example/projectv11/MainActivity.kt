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

class MainActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var welcomeText: TextView

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-api-base-url/") //
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService: AuthService = retrofit.create(AuthService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        welcomeText = findViewById(R.id.wilkommen)
    }

    fun login(view: android.view.View) {
        val enteredUsername = usernameInput.text.toString()
        val enteredPassword = passwordInput.text.toString()

        // Send a login request to the server using Retrofit
        val loginRequest = LoginRequest(enteredUsername, enteredPassword)
        authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse?.success == true) {
                        // Login successful
                        welcomeText.text = "Login Successful, please continue."

                        val sessionToken = loginResponse.sessionToken ?: ""

                        // Delay for usability
                        Handler().postDelayed({
                            // Start activity
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
