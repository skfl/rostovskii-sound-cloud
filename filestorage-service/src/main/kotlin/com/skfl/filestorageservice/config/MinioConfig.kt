package com.skfl.filestorageservice.config

import io.minio.BucketExistsArgs
import io.minio.ListObjectsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.errors.ErrorResponseException
import io.minio.errors.InsufficientDataException
import io.minio.errors.InternalException
import io.minio.errors.InvalidResponseException
import io.minio.errors.XmlParserException
import java.io.IOException
import java.rmi.ServerException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MinioConfig(
    @Value("\${app.minio.minioUrl}")
    private val minioUrl: String,
    @Value("\${app.minio.minioUsername}")
    private val minioUsername: String,
    @Value("\${app.minio.minioPassword}")
    private val minioPassword: String,
    @Value("\${app.minio.bucketName}")
    private val bucketName: String,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    @Throws(Exception::class)
    fun minioClient(): MinioClient {
        val client: MinioClient = MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(minioUsername, minioPassword)
            .build()
        createDefaultBucket(client)
        if (client.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .build()
            ).iterator().hasNext()
        ) {
        }
        return client
    }

    @Throws(
        ErrorResponseException::class,
        InsufficientDataException::class,
        InternalException::class,
        InvalidKeyException::class,
        InvalidResponseException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        ServerException::class,
        XmlParserException::class
    )
    fun createDefaultBucket(client: MinioClient) {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
            log.info("Minio bucket {} created successfully", bucketName)
        }
    }
}