package com.example.projectv11

// Data class representing a login request
data class LoginRequest(val username: String, val password: String)

// Data class representing a login response
data class LoginResponse(val success: Boolean, val sessionToken: String?)
