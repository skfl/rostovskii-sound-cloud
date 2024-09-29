package com.skfl.filestorageservice.mapper

import com.skfl.filestorageservice.domain.File
import com.skfl.filestorageservice.domain.dto.FileUploadResponse
import org.springframework.stereotype.Component

@Component
class FileMapper {

    fun fileToFileUploadResponse(file: File): FileUploadResponse =
        FileUploadResponse(
            privateLink = file.privateLink,
            name = file.name,
            createdAt = file.createdAt,
            extension = file.extension
        )
}