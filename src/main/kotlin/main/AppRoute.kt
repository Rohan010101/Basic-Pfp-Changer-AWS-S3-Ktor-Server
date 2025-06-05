package com.example.main

import aws.sdk.kotlin.services.s3.S3Client
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Suppress("DEPRECATION")
fun Route.profilePictureRoute() {

    // POST: Upload profile picture to S3
    post("/uploadPfp") {
        val multipart = call.receiveMultipart()
        var imageBytes: ByteArray? = null

        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                imageBytes = part.streamProvider().readBytes()
            }
            part.dispose()
        }

        if (imageBytes != null) {
            try {
                S3Client.fromEnvironment { }.use { s3 ->
//                deletePfp(s3)
                    insertPfp(s3, imageBytes!!)
                }
                call.respond(
                    status = HttpStatusCode.OK,
                    message = "✅ New Profile picture uploaded to S3"
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed: ${e.message}")
            }
        } else {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "No image provided"
            )
            return@post
        }
    }

    // GET: Get presigned URL for the current profile picture
    get("/getPfpUrl") {
        S3Client.fromEnvironment {  }.use { s3 ->
            val presignedUrl = getPfpPresigned(s3)
            call.respond(HttpStatusCode.OK, mapOf("url" to presignedUrl))
        }
    }
}