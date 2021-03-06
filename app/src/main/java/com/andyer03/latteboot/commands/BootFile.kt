package com.andyer03.latteboot.commands

import com.andyer03.latteboot.other.Device
import java.io.File

class BootFile {
    fun check(): String {
        System("mountEFI").boot()
        BootExec().copyTempBoot()
        val file = File(Com().tempBoot)

        return when (Hash().crc32(file)) {
            Device().windowsHash -> {
                BootExec().delTempBoot()
                "Windows"
            }
            Device().androidHash -> {
                BootExec().delTempBoot()
                "Android"
            }
            else -> {
                BootExec().delTempBoot()
                "Error"
            }
        }
    }
}