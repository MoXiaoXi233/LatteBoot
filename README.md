<p align="center">
<a href="https://github.com/AndyER03/LatteBoot/"><img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" /></a>
</p>
<p align="center">
   <b><a href="https://github.com/AndyER03/LatteBoot/">LatteBoot</a></b>
</p>
<p align="center">
   Advanced boot options for MI Pad 2 tablet
</p>

### Features:
* Small app size
* Simple reboot by one click
* App supports dualboot *(Android + Windows)*
* *App checking for EFI BIOS OS boot availability by boot file in EFI partition exists and show or hide Windows boot option depending on it*

### For booting to Android modes and Windows:
* Just grant app Root permission

### For Dual Boot users:
* Preparing EFI partition:
    * [**Boot files**](https://drive.google.com/drive/folders/1Son2vUjhO53f5fJRGg-mvrW7H79grvHo?usp=sharing "Google Drive") must be placed to mmcblk0p8 to /EFI/BOOT/ in advance *(You can do this with diskpart utility in Windows or WinPE booted on MI Pad 2 tablet)*
    * bootx64.efi.win file must be renamed to bootx64.efi to boot into Windows (then bootx64.efi.miui file to be placed near is needed)
    * bootx64.efi.miui file must be renamed to bootx64.efi to boot into Android (then bootx64.efi.win file to be placed near is needed)
    
* How boot to Windows works:
    * App mounts boot partition to mnt folder and changes the names of boot files with efi extension
    * bootx64.efi -> bootx64.efi.miui
    * bootx64.efi.win -> bootx64.efi
---
<p align="center">
Screenshots:
</p>
<p align="center">
   <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_1.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_2.png" width=30% height=25%>
</p>

---
<p align="right">
By AndyER03
</p>
