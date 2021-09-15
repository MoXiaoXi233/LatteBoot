package com.andyer03.latteboot.commands

import com.andyer03.latteboot.commands.Com as C

class BootExec {
    fun reboot() {
        Runtime.getRuntime().exec(C().reboot)
    }
    fun mountEFI() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().remount)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().cifs)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().efi)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().umount)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().mount)).waitFor()
    }
    fun windows() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().noMIUI)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().win)).waitFor()
    }
    fun android() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().noWIN)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().miui)).waitFor()
    }
    fun recovery() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().recovery))
    }
    fun bootloader() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().bootloader))
    }
    fun dnx() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().dnx))
    }
    fun shutdown() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().shutdown))
    }
    fun safeMode() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().safeMode)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().reboot))
    }
    fun screenOff() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().screenOff))
    }

    fun copyTempBoot() {
        Runtime.getRuntime().exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi /sdcard/bf").waitFor()
    }
    fun delTempBoot() {
        Runtime.getRuntime().exec("rm /storage/emulated/0/bf").waitFor()
    }
}