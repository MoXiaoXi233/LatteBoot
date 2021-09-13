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
* App supports Dual Boot *(Android + Windows)*
* *App checking for EFI BIOS OS boot availability by boot file in EFI partition exists and show or hide Windows boot option depending on it*

### For booting to Safe mode and Windows:
* Grant app Root permission and click on preferred card

### For booting to other modes:
* Just click on preferred card *(Root permissions not required)*

### For Dual Boot users:
* EFI partition preparation:
    * [**Boot files**](https://drive.google.com/drive/folders/1Son2vUjhO53f5fJRGg-mvrW7H79grvHo?usp=sharing "Google Drive") must be placed to **"/dev/block/platform/pci\*/\*/by-name/\*loader"** block to **/EFI/BOOT/** in advance *(You can do this with diskpart utility in Windows or WinPE booted on MI Pad 2 tablet by choosing 7th or 8th part with "System" label)*
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
<img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_1.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_2.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_3.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_4.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_5.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_6.png" width=30% height=25%>
</p>

---
<p align="right">
By AndyER03
</p>

[**Russian README**](https://github.com/AndyER03/LatteBoot/blob/master/README.ru.md "Russian README")
