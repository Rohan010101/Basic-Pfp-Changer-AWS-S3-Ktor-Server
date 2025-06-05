package com.example.main

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.sdk.kotlin.services.s3.presigners.presignGetObject
import aws.sdk.kotlin.services.s3.presigners.presignPutObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.time.Duration.Companion.hours


val BUCKET = "aws-kotlin-test-app"
val KEY = "profilePic/pfp.jpg"

val client = HttpClient(CIO)

suspend fun insertPfp(
    s3: S3Client,
    imageBytes: ByteArray
) {
    val unsignedPutObjectRequest = PutObjectRequest{
        bucket = BUCKET
        key = KEY
//        body = ByteStream.fromBytes(imageBytes)
//        contentType = "image/jpeg"
    }

    val presignedPutObjectRequest = s3.presignPutObject(
        input = unsignedPutObjectRequest,
        duration = 24.hours
    )
//    s3.putObject(presignedPutObjectRequest)

    val response:HttpResponse = client.put(presignedPutObjectRequest.url.toString()) {
        presignedPutObjectRequest.headers.forEach{ key, values ->
            headers.appendAll(key,values)
        }
        setBody(imageBytes)
    }

    println("S3 Upload Response Code: ${response.status}")
    println("S3 Upload Response Body: ${response.bodyAsText()}")


    if (!response.status.isSuccess()) {
        throw Exception("Failed to upload: ${response.status}")
    }
    println("✅ Uploaded: $BUCKET/$KEY")
}


suspend fun deletePfp(s3: S3Client) {
    val deleteObjectRequest = DeleteObjectRequest {
        bucket = BUCKET
        key = KEY
    }
    s3.deleteObject(deleteObjectRequest)
    println("🗑️ Deleted old PFP: $BUCKET/$KEY")
}


suspend fun getPfpPresigned(s3: S3Client): String {

    // Create a GetObjectRequest
    val unsignedGetObjectRequest = GetObjectRequest {
        bucket = BUCKET
        key = KEY
    }

    // Presign the GetObject request
    val presignedGetObjectRequest = s3.presignGetObject(
        input = unsignedGetObjectRequest,
        duration = 24.hours
    )

//    val objectContents = URL(presignedGetObjectRequest.url.toString()).readText()
//    return objectContents

    return presignedGetObjectRequest.url.toString()
}