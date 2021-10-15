<img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" align="right"/>

# LatteBoot
Advanced boot options for Xiaomi MI Pad 2
## Features
* Reboot
  * Simple reboot
  * Safe mode
  * Recovery
  * Fastboot
  * DNX
  * Windows *(if supported)*
* Screen off
* Bootloader swap
* Shortcuts

## Requirements
* Grant root permission
## Dual boot preparation
Do the following steps only if you have Windows as the alternative OS installed on yout tablet in common with Android!
* Place [**boot files**](https://drive.google.com/drive/folders/1Son2vUjhO53f5fJRGg-mvrW7H79grvHo "Google Drive") to `/dev/block/platform/pci\*/\*/by-name/\*loader` block to `/EFI/BOOT/` subdirectory
* `bootx64.efi.win` for boot to Windows
* `bootx64.efi.miui` for boot to Android
* Leave only `.efi` extension for preferred OS
## User guide
* Click on the card to reboot
* Click `Swap bootloader` to do it without reboot
* Menu items switch shortcuts visibility in launcher
## Bugs
* App crashes on deny root permission
* App crashes if there is no any boot file with allowed name in EFI block
