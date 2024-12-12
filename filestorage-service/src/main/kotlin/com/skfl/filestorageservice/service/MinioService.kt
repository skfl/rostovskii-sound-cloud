package com.skfl.filestorageservice.service

import com.skfl.filestorageservice.helper.FileHelper
import com.skfl.filestorageservice.helper.FileHelper.Companion.generateFilename
import io.minio.MinioClient
import io.minio.UploadObjectArgs
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MinioService(
    private val minioClient: MinioClient
) {

    fun upload(multipartFile: MultipartFile) {
        minioClient.uploadObject(
            UploadObjectArgs.builder()
                .filename(generateFilename())

                .build()
        )
    }
}
