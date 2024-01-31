package com.example.projectv11

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.security.KeyStore
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.KeyGenerator

// PhoneActivity for file encryption, hashing, and communication with an FTP server
class PhoneActivity : AppCompatActivity() {
    private val PICK_FILE_REQUEST_CODE = 1
    private val selectedFiles: MutableList<Uri> = mutableListOf()
    private lateinit var selectedFilesTextView: TextView
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val HASH_PREF = "hash_value"
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    private val KEY_GENERATED_PREF = "key_generated"
    private val KEY_ALIAS = "encryption_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone)

        val homeButton = findViewById<Button>(R.id.hme3)
        homeButton.setOnClickListener {
            val intent5 = Intent(this, MainActivity::class.java)
            startActivity(intent5)
        }

        val selectFileButton: Button = findViewById(R.id.selectFileButton)
        val sendButton: Button = findViewById(R.id.sendButton)
        selectedFilesTextView = findViewById(R.id.selectedFilesTextView)

        selectFileButton.setOnClickListener {
            openFileManager()
        }

        // Check if the encryption key has been generated
        if (!prefs.getBoolean(KEY_GENERATED_PREF, false)) {
            generateEncryptionKey()
            prefs.edit().putBoolean(KEY_GENERATED_PREF, true).apply()
        }

        sendButton.setOnClickListener {
            selectedFiles.forEach { uri ->
                val encryptedData = encryptFile(uri)
                val hashedData = hashData(encryptedData)

                // Store the hash value locally
                prefs.edit().putString(HASH_PREF, hashedData).apply()

                // Send the hash value to the FTP server
                sendHashToFTPServer(hashedData)
            }
        }
    }

    private fun openFileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedFileUri = data?.data
            selectedFileUri?.let {
                selectedFiles.add(it)
                updateSelectedFilesTextView()
            }
        }
    }

    private fun updateSelectedFilesTextView() {
        val fileNames = selectedFiles.map { uri ->
            getFileNameFromUri(uri)
        }
        selectedFilesTextView.text = "Selected Files:\n${fileNames.joinToString("\n")}"
    }

    private fun getFileNameFromUri(uri: Uri): String {
        return uri.lastPathSegment ?: "Unknown File"
    }

    private fun generateEncryptionKey() {
        try {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()

            // Key generation successful
        } catch (e: Exception) {
            // Handle key generation failure
            e.printStackTrace()
        }
    }

    private fun encryptFile(uri: Uri): ByteArray {
        // Retrieve the encryption key from the keystore
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val secretKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
        val secretKey = secretKeyEntry.secretKey

        // Use the retrieved key for encryption
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // Implement your encryption logic using Cipher class
        // For simplicity, let's assume uri points to a text file
        val fileContent = contentResolver.openInputStream(uri)?.readBytes() ?: byteArrayOf()

        // Return the encrypted data
        return cipher.doFinal(fileContent)
    }

    private fun hashData(data: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(data)
        return Base64.encodeToString(hashedBytes, Base64.DEFAULT)
    }

    private fun sendHashToFTPServer(localHash: String) {
        val request = Request.Builder()
            .url("https://your-ftp-server-url") // Replace with your FTP server URL
            .post(localHash.toRequestBody("text/plain".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure to connect to the FTP server
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val serverHash = response.body?.string()

                    // Compare the server's hash with the locally stored hash
                    if (serverHash == localHash) {
                        // Hashes match, do something
                        runOnUiThread {
                            // Update UI or take further actions
                        }
                    } else {
                        // Hashes do not match, do something else
                        runOnUiThread {
                            // Update UI or take further actions
                        }
                    }
                } else {
                    // Handle server response error
                    runOnUiThread {
                        // Update UI or take further actions
                    }
                }
            }
        })
    }
}



