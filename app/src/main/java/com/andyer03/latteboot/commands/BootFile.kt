package com.andyer03.latteboot.commands

import com.andyer03.latteboot.other.Device
import java.io.File

class BootFile {

    @ExperimentalStdlibApi
    fun checkBoot(): String {
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

    @ExperimentalStdlibApi
    fun checkWin(): Boolean {
        System("mountEFI").boot()
        BootExec().copyTempAnotherBoot()
        val file = File(Com().tempBoot)
        val file2 = File(Com().tempBoot2)

        return if ((Hash().crc32(file) == Device().windowsHash) || (Hash().crc32(file2) == Device().windowsHash)) {
            BootExec().delTempBoot()
            BootExec().delTempAnotherBoot()
            true
        } else {
            BootExec().delTempBoot()
            BootExec().delTempAnotherBoot()
            false
        }
    }
}