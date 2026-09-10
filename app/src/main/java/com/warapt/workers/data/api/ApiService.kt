package com.warapt.workers.data.api

import com.warapt.workers.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResult>

    @GET("Workers")
    suspend fun getWorkers(@Query("dni") dni: String? = null): Response<List<Worker>>

    @POST("Workers")
    suspend fun addWorker(@Body request: WorkerRequest): Response<Unit>
}