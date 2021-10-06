<img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" align="right"/>

# LatteBoot
> Advanced boot options for Xiaomi MI Pad 2 tablet
## Features
* Small app size
* Simple reboot
* Shortcuts
* Reboot to Windows OS
* Swap boot files without reboot
## Tutorial
* Just click on a preferred card for reboot
* Click on ```Swap bootloader``` to swap boot files without reboot
* Click on menu item to enable or disable shortcut
## EFI partition preparation for dual boot users
* [**Boot files**](https://drive.google.com/drive/folders/1Son2vUjhO53f5fJRGg-mvrW7H79grvHo "Google Drive") must be placed to ```/dev/block/platform/pci\*/\*/by-name/\*loader``` block to ```/EFI/BOOT/```
* ```bootx64.efi.win``` for boot to Windows
* ```bootx64.efi.miui``` for boot to Android
* Just leave only ```.efi``` extension for preferred OS
