package com.andyer03.latteboot.commands

import java.io.*
import java.util.zip.CRC32
import java.util.zip.CheckedInputStream

class Hash {

    @ExperimentalStdlibApi
    fun crc32(f: File): String {

        try {
            val file = FileInputStream(f)
            val check = CheckedInputStream(file, CRC32())
            val `in` = BufferedInputStream(check)
            while (`in`.read() != -1) {
                // Read file in completely
            }
            `in`.close()

            return check.checksum.value.toULong().toString(16)

        }
        catch (ex: Exception) {
            when (ex) {
                is FileNotFoundException,
                is NumberFormatException,
                is IOException -> {
                    ex.printStackTrace() // handle
                }
                else -> throw ex
            }
            return ""
        }
    }
}