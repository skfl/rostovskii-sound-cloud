package com.skfl.filestorageservice.domain.dto

import java.time.Instant

data class FileUploadResponse(
    val privateLink: String,
    val name: String,
    val createdAt: Instant,
    val extension: String
)
