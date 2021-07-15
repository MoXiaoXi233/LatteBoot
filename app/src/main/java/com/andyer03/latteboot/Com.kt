package com.andyer03.latteboot

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
    val miui = "mv $efiPath/EFI/BOOT/$file $efiPath/EFI/BOOT/$file.miui"
    val win = "mv $efiPath/EFI/BOOT/$file.win $efiPath/EFI/BOOT/$file"

    val reboot = "reboot"
    val recovery = "reboot recovery"
    val bootloader= "reboot bootloader"
    val dnx = "reboot dnx"
    val shutdown = "reboot -p"
    val safemode = "echo \"1\" > /data/property/persist.sys.safemode\n"
    val screenoff = "input keyevent 26"
}