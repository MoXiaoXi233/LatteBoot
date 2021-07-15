package com.andyer03.latteboot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.andyer03.latteboot.databinding.ActivityMainBinding
import java.io.File

open class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = BootAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Device().check()) {
            Toast.makeText(this, R.string.mi_pad_2_toast, Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            init()
        }
    }

    private fun init() = with(binding) {
        val imageIdList = listOf(
            R.drawable.ic_restart,
            R.drawable.ic_shield,
            R.drawable.ic_shutdown,
            R.drawable.ic_recovery,
            R.drawable.ic_bootloader,
            R.drawable.ic_bootloader,
            R.drawable.ic_shutdown,
            R.drawable.ic_windows
        )

        val titleIdList = listOf(
            R.string.reboot_device_title,
            R.string.reboot_safemode_title,
            R.string.reboot_screenoff_title,
            R.string.reboot_recovery_title,
            R.string.reboot_bootloader_title,
            R.string.reboot_dnx_title,
            R.string.reboot_shutdown_title,
            R.string.reboot_win_title
        )

        rcView.layoutManager = GridLayoutManager(this@MainActivity, 3)
        rcView.adapter = adapter

        val reboot = BootOptions(imageIdList[0], titleIdList[0].toString())
        val safemode = BootOptions(imageIdList[1], titleIdList[1].toString())
        val scrnoff = BootOptions(imageIdList[2], titleIdList[2].toString())
        val recovery = BootOptions(imageIdList[3], titleIdList[3].toString())
        val bootloader = BootOptions(imageIdList[4], titleIdList[4].toString())
        val dnx = BootOptions(imageIdList[5], titleIdList[5].toString())
        val shutdown = BootOptions(imageIdList[6], titleIdList[6].toString())
        val windows = BootOptions(imageIdList[7], titleIdList[7].toString())

        adapter.addBootOptions(reboot)
        adapter.addBootOptions(safemode)
        adapter.addBootOptions(scrnoff)
        adapter.addBootOptions(recovery)
        adapter.addBootOptions(bootloader)
        adapter.addBootOptions(dnx)
        adapter.addBootOptions(shutdown)

        val tempFile = Environment.getExternalStorageDirectory().path + "/.latteboot"
        val fileName = File(tempFile).isFile
        if (!fileName) {
            System("mountefi").boot()
            Runtime.getRuntime().exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi.win /sdcard/.latteboot")
            if (fileName) {
                adapter.addBootOptions(windows)
            }
        }
    }
}