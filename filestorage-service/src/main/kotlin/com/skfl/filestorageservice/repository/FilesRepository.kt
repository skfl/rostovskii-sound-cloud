package com.skfl.filestorageservice.repository

import com.skfl.filestorageservice.domain.File
import com.skfl.filestorageservice.exception.FileStorageException
import com.skfl.filestorageservice.exception.SAVE_FILE_DATA_TO_DB_EXCEPTION
import com.skfl.filestorageservice.jooq.files.Tables.FILES
import com.skfl.filestorageservice.jooq.files.tables.pojos.JFiles
import java.time.LocalDateTime
import java.time.ZoneId
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
class FilesRepository(
    @Qualifier("fsJooqDslContext")
    private val ctx: DSLContext
) {

    fun save(file: File): JFiles {
        return ctx.insertInto(FILES)
            .set(FILES.NAME, file.name)
            .set(FILES.EXTENSION, file.extension)
            .set(FILES.CREATED_AT, LocalDateTime.ofInstant(file.createdAt, ZoneId.systemDefault()))
            .set(FILES.UPDATED_AT, LocalDateTime.ofInstant(file.updatedAt, ZoneId.systemDefault()))
            .set(FILES.PRIVATE_LINK, file.privateLink)
            .set(FILES.PUBLIC_LINK, file.publicLink)
            .returning()
            .fetchOneInto(JFiles::class.java) ?: throw FileStorageException(SAVE_FILE_DATA_TO_DB_EXCEPTION)
    }
}
