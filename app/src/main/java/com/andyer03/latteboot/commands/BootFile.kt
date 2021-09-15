package com.andyer03.latteboot.commands

import android.annotation.SuppressLint
import com.andyer03.latteboot.other.Device
import java.io.File

class BootFile {
    @SuppressLint("SdCardPath")
    fun check(): Boolean {
        System("mountEFI").boot()
        BootExec().copyTempBoot()
        val file = File(Com().tempBoot)

        return when (Hash().crc32(file)) {
            Device().windowsHash -> {
                BootExec().delTempBoot()
                true
            }
            Device().androidHash -> {
                BootExec().delTempBoot()
                false
            }
            else -> {
                BootExec().delTempBoot()
                false
            }
        }
    }
}