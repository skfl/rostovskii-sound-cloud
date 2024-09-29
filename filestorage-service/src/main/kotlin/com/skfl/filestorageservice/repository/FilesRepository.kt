package com.skfl.filestorageservice.repository

import com.skfl.filestorageservice.domain.File
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
class FilesRepository(
    @Qualifier("fsJooqDslContext")
    private val ctx: DSLContext
) {

    fun save(file: File) {
    }
}