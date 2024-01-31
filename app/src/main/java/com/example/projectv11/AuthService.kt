package com.example.projectv11

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Service interface for authentication requests
interface AuthService {

    // Retrofit annotation indicating that this method will perform a POST request to the "login" endpoint
    @POST("login")
    // Function to perform login operation, taking a LoginRequest as the request body
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
