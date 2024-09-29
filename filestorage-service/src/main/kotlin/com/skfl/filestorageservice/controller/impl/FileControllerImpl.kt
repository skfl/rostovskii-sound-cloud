package com.skfl.filestorageservice.controller.impl

import com.skfl.filestorageservice.controller.FileController
import com.skfl.filestorageservice.domain.dto.FileUploadResponse
import com.skfl.filestorageservice.mapper.FileMapper
import com.skfl.filestorageservice.service.FilesService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileControllerImpl(
    private val filesService: FilesService,
    private val fileMapper: FileMapper
) : FileController {

    @PostMapping("/file")
    override fun upload(multipartFile: MultipartFile): FileUploadResponse {
        val uploadedFile = filesService.upload(multipartFile)
        return fileMapper.fileToFileUploadResponse(uploadedFile)
    }
}