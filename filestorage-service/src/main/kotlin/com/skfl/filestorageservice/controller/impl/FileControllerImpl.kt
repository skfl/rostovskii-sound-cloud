package com.skfl.filestorageservice.controller.impl

import com.skfl.filestorageservice.controller.FileController
import com.skfl.filestorageservice.domain.dto.FileUploadResponse
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileControllerImpl : FileController {

    override fun upload(multipartFile: MultipartFile): FileUploadResponse {
            TODO("Not yet implemented")
    }
}