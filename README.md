# LatteBoot
#### Advanced boot options for MI Pad 2 tablet
![](https://raw.githubusercontent.com/AndyER03/WinBoot/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

### Features:
* Small app size
* Simple reboot by one click
* App supports dualboot *(Android + Windows)*
*App checking for EFI BIOS OS boot availability by checking efi boot file in EFI partition exists and show or hide Windows boot option depending on it*

#### For booting to Android modes and Windows:
* Just grant app Root permission

#### For Dual Boot users:
* Preparing to EFI BIOS OS (such as Windows) boot availability:
    * [**Boot files**](https://drive.google.com/drive/folders/1Son2vUjhO53f5fJRGg-mvrW7H79grvHo?usp=sharing "Google Drive") must be placed to mmcblk0p8 to /EFI/BOOT/ in advance
    * *(Be careful)* delete exists bootx64.efi file
    * bootx64.efi.win file must be renamed to bootx64.efi to boot into Windows (then bootx64.efi.miui file to be placed near is needed)
    * bootx64.efi.miui file must be renamed to bootx64.efi to boot into Android (then bootx64.efi.win file to be placed near is needed)
    
* How booting to Windows OS works:
    * App mounts boot partition to mnt folder and changes the names of boot files with efi extension
      * bootx64.efi -> bootx64.efi.miui
      * bootx64.efi.win -> bootx64.efi
