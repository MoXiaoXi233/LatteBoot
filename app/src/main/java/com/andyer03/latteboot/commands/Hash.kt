package com.andyer03.latteboot.commands

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.CRC32
import java.util.zip.CheckedInputStream

class Hash {

    fun crc32(f: File): String {

        val file = FileInputStream(f)
        val check = CheckedInputStream(file, CRC32())
        val `in` = BufferedInputStream(check)
        while (`in`.read() != -1) {
            // Read file in completely
        }
        `in`.close()

        return check.checksum.value.toULong().toString(16)
    }

}