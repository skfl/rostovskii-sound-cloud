package com.skfl.filestorageservice.helper

import java.util.UUID

class FileHelper {

    companion object {

        fun generateFilename() : String {
            return UUID.randomUUID().toString()
        }
    }
}