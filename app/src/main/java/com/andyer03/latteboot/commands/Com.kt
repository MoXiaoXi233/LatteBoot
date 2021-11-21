package com.andyer03.latteboot.commands

import android.os.Environment

class Com {
    private val file = "bootx64.efi"
    private val efiPath = "/mnt/cifs/efi"

    val su = "/system/xbin/su"
    val c = "-c"
    val remount = "mount -o remount rw /"
    val cifs = "mkdir /mnt/cifs"
    val efi = "mkdir /mnt/cifs/efi"
    val umount = "umount $efiPath"
    val mount = "mount | grep $efiPath > /dev/null 2>&1 || mount -t vfat /dev/block/platform/pci*/*/by-name/*loader $efiPath"
    val noMIUI = "mv $efiPath/EFI/BOOT/$file $efiPath/EFI/BOOT/$file.miui"
    val win = "mv $efiPath/EFI/BOOT/$file.win $efiPath/EFI/BOOT/$file"
    val noWIN = "mv $efiPath/EFI/BOOT/$file $efiPath/EFI/BOOT/$file.win"
    val miui = "mv $efiPath/EFI/BOOT/$file.miui $efiPath/EFI/BOOT/$file"
    val tempBoot = Environment.getExternalStorageDirectory().path + "/bf"

    val safeMode = "setprop persist.sys.safemode 1"
    val reboot = "reboot"
    val recovery = "recovery"
    val bootloader= "bootloader"
    val dnx = "dnx"
    val shutdown = "-p"
    val screenOff = "input keyevent KEYCODE_POWER"

    private val efiMIUISDcard = Environment.getExternalStorageDirectory().path + "/" + file
    private val efiWinSDcard = Environment.getExternalStorageDirectory().path + "/" + file + ".win"

    val cleanEfi = "rm $efiPath/*"
    val copyEfiMIUI = "mv $efiMIUISDcard $efiPath/EFI/BOOT/$file"
    val copyEfiWIN = "mv $efiWinSDcard $efiPath/EFI/BOOT/$file.win"
}