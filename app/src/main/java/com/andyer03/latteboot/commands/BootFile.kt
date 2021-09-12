package com.andyer03.latteboot.commands

import android.annotation.SuppressLint
import com.andyer03.latteboot.other.Device
import java.io.File

class BootFile {
    @SuppressLint("SdCardPath")
    fun check(): Boolean {
        System("mountefi").boot()
        BootExec().copytempboot()
        val file = File(Com().tempboot)

        return when (Hash().crc32(file)) {
            Device().windowsHash -> {
                BootExec().deltempboot()
                true
            }
            Device().androidHash -> {
                BootExec().deltempboot()
                false
            }
            else -> {
                BootExec().deltempboot()
                false
            }
        }
    }
}