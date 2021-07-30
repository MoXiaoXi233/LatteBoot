<p align="center">
<a href="https://github.com/AndyER03/LatteBoot/"><img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" /></a>
</p>
<p align="center">
   <b><a href="https://github.com/AndyER03/LatteBoot/">LatteBoot</a></b>
</p>
<p align="center">
   Расширенное меню загрузки планшета MI Pad 2
</p>

### Возможности:
* Малый размер приложения
* Простая перезагрузка по клику
* Поддерживается Dual Boot *(Android + Windows)*
* *Приложение проверяет доступность загрузки через EFI BIOS с помощью поиска наличия загрузочного файоа в разделе EFI, и в зависимости от этого показывает/скрывает карточку для загрузки в OS Windows*

### Для загрузки в безопасный режим и в Windows:
* Предоставьте Root доступ и выберите карточку

### Для загрузки в остальные режимы:
* Просто нажмите на нужную карточку *(Root права не требуются)*

### Для пользователей Dual Boot:
* Подготовка EFI раздела:
    * [**Загрузочные файлы**](https://drive.google.com/drive/folders/1Son2vUjhO53f5fJRGg-mvrW7H79grvHo?usp=sharing "Google Drive") должны быть размещены в разделе по пути **"/dev/block/platform/pci\*/\*/by-name/\*loader"**, а именно в директорию **/EFI/BOOT/** *(Можно сделать через утилиту diskpart в Windows или в WinPE на планшете MI Pad 2)*
    * Файл bootx64.efi.win должен быть назван bootx64.efi для загрузки в Windows (также, рядом необходимо разместить файл bootx64.efi.miui)
    * Файл bootx64.efi.miui должен быть назван bootx64.efi для загрузки в Android (также,рядом необходимо разместить файл bootx64.efi.win)
    
* Как работает загрузка Windows:
    * Приложение подключает загрузочный раздел в директорию mnt и меняет местами расширения имен загрузочных файлов
    * bootx64.efi -> bootx64.efi.miui
    * bootx64.efi.win -> bootx64.efi
---
<p align="center">
Скриншоты:
</p>
<p align="center">
   <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_1.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_2.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_3.png" width=30% height=25%> <img src="https://raw.githubusercontent.com/AndyER03/LatteBoot/master/Screenshots/Scrn_4.png" width=30% height=25%>
</p>

---
<p align="right">
By AndyER03
</p>

[**Английский README**](https://github.com/AndyER03/LatteBoot/blob/master/README.md "Английский README")
