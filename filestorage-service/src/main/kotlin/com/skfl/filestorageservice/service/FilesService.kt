package com.skfl.filestorageservice.service

import com.skfl.filestorageservice.domain.File
import com.skfl.filestorageservice.repository.FilesRepository
import java.time.Instant
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FilesService(
    private val filesRepository: FilesRepository
) {

    fun upload(multipartFile: MultipartFile): File {
        return File("", "", "", "", Instant.now(), Instant.now())
    }
}
