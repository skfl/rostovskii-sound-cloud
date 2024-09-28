package com.skfl.filestorageservice.controller

import com.skfl.filestorageservice.domain.dto.FileUploadResponse
import org.springframework.web.multipart.MultipartFile

interface FileController {

    fun upload(multipartFile: MultipartFile) : FileUploadResponse
}