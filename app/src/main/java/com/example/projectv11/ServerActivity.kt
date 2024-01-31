package com.example.projectv11

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

// ServerActivity for interacting with the server endpoints
class ServerActivity : AppCompatActivity() {
    private val FILES_ENDPOINT = "https://your-endpoint-url/get_files"
    private val REQUEST_ENDPOINT = "https://your-endpoint-url/send_request"

    private lateinit var messageEditText: EditText
    private lateinit var filesTextView: TextView
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.server)

        val homeButton = findViewById<Button>(R.id.home1)
        homeButton.setOnClickListener {
            val intent5 = Intent(this, MainActivity::class.java)
            startActivity(intent5)
        }

        // Initialize UI elements
        messageEditText = findViewById(R.id.messageEditText)
        filesTextView = findViewById(R.id.filesTextView)
        errorTextView = findViewById(R.id.errorTextView)

        // Set click listeners for buttons
        val sendMessageButton: Button = findViewById(R.id.sendMessageButton)
        val requestButton: Button = findViewById(R.id.requestButton)

        sendMessageButton.setOnClickListener {
            // Send a message to the endpoint to get the list of files
            val message = messageEditText.text.toString()
            getFilesFromEndpoint(message)
        }

        requestButton.setOnClickListener {
            // Send the selected files back to the endpoint
            val selectedFiles = filesTextView.text.toString()
            sendFilesToEndpoint(selectedFiles)
        }
    }

    // Function to get the list of files from the endpoint
    private fun getFilesFromEndpoint(message: String) {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("$FILES_ENDPOINT?message=$message")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showError("Failed to get files from the endpoint.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val filesList = response.body?.string()
                    runOnUiThread {
                        filesTextView.text = filesList
                    }
                } else {
                    showError("Failed to get files from the endpoint.")
                }
            }
        })
    }

    // Function to send selected files to the endpoint
    private fun sendFilesToEndpoint(selectedFiles: String) {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        val requestBody = selectedFiles.toRequestBody("text/plain".toMediaType())

        val request = Request.Builder()
            .url(REQUEST_ENDPOINT)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showError("Failed to send files to the endpoint.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val serverResponse = response.body?.string()
                    runOnUiThread {
                        handleServerResponse(serverResponse)
                    }
                } else {
                    showError("Failed to send files to the endpoint.")
                }
            }
        })
    }

    // Function to handle the server response after sending files
    private fun handleServerResponse(serverResponse: String?) {
        // Assuming the server responds with "true" or "false"
        if (serverResponse == "true") {
            // Files are valid, proceed with saving them to the app's files
            saveFilesToLocalDirectory()
        } else {
            // Files are not valid, show an error or retry logic
            showError("Files are not valid.")
        }
    }

    // Function to save files to the app's local directory
    private fun saveFilesToLocalDirectory() {
        // Implement logic to save files to the app's files directory
        // ...

        // Example: display a success message
        runOnUiThread {
            showError("Files saved successfully.")
        }
    }

    // Function to display error messages in the UI
    private fun showError(errorMessage: String) {
        runOnUiThread {
            errorTextView.text = errorMessage
        }
    }
}
