package com.skfl.filestorageservice.domain

import java.time.Instant

data class File(
    val privateLink: String,
    val publicLink: String,
    val name: String,
    val extension: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
