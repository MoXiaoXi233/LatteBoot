package com.andyer03.latteboot.commands

import java.io.FileNotFoundException
import java.io.IOException

@ExperimentalStdlibApi
class BootAnotherMode {

    fun boot(os1: String, os2: String, target: String, bootloader: String) {
        try {
            when {
                BootFile().checkBoot() == os1 -> {
                    System(target).boot()
                }
                BootFile().checkBoot() == os2 -> {
                    System(bootloader).boot()
                    System(target).boot()
                }
            }
        } catch (ex: Exception) {
            when (ex) {
                is FileNotFoundException,
                is NumberFormatException,
                is IOException -> {
                    ex.printStackTrace() // handle
                }
                else -> throw ex
            }
        }
    }

    fun swap() {
        if (BootFile().checkBoot() == "Android") {
            System("win").boot()
        } else if (BootFile().checkBoot() == "Windows") {
            System("and").boot()
        }
    }
}