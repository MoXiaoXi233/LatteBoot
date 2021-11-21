package com.andyer03.latteboot.commands

import com.andyer03.latteboot.commands.Com as C

class BootExec {
    fun shutdown() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().shutdown)) // No-Root
    }
    fun reboot() {
        Runtime.getRuntime().exec(C().reboot) // No-Root
    }
    fun recovery() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().recovery)) // No-Root
    }
    fun bootloader() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().bootloader)) // No-Root
    }
    fun dnx() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().dnx)) // No-Root
    }
    fun screenOff() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().screenOff)) // Root
    }
    fun safeMode() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().safeMode)).waitFor() // Root
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().reboot))
    }
    fun windows() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().noMIUI)).waitFor() // Root
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().win)).waitFor()
    }
    fun android() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().noWIN)).waitFor() // Root
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().miui)).waitFor()
    }

    fun mountEFI() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().remount)).waitFor() // Root
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().cifs)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().efi)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().umount)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().mount)).waitFor()
    }
    fun cleanEFI() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().cleanEfi)).waitFor() // Root
    }
    fun copyEFI() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().copyEfiMIUI)).waitFor() // Root
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().copyEfiWIN)).waitFor()
    }
    fun delWinEFI() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().delEfiWIN)).waitFor() // Root
    }

    fun copyTempBoot() {
        Runtime.getRuntime().exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi /storage/emulated/0/bf").waitFor()
    }
    fun delTempBoot() {
        Runtime.getRuntime().exec("rm /storage/emulated/0/bf").waitFor()
    }

    fun copyTempAnotherBoot() {
        Runtime.getRuntime().exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi.win /storage/emulated/0/bf2").waitFor()
    }
    fun delTempAnotherBoot() {
        Runtime.getRuntime().exec("rm /storage/emulated/0/bf2").waitFor()
    }
}