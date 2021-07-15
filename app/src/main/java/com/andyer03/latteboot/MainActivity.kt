package com.andyer03.latteboot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
            val tempFile = Environment.getExternalStorageDirectory().path + "/.latteboot"
            val fileName = File(tempFile).isFile
            if (!fileName) {
                System("mountefi").boot()
                Runtime.getRuntime().exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi.win /sdcard/.latteboot")
                if (!fileName) {
                    hideStack("2")
                }
            }
            init()

            val and = findViewById<ImageView>(R.id.imageAndroid)
            val win = findViewById<ImageView>(R.id.imageWindows)
            val rec = findViewById<ImageView>(R.id.imageRecovery)
            val fbt = findViewById<ImageView>(R.id.imageBootloader)
            val dnx = findViewById<ImageView>(R.id.imageDNX)
            val pwd = findViewById<ImageView>(R.id.imageShutdown)
            val sfm = findViewById<ImageView>(R.id.imageSafeMode)
            val scn = findViewById<ImageView>(R.id.imageScrnOff)

            and.setOnClickListener {
                hide("and")
                System("and").boot()
            }
            win.setOnClickListener {
                hide("win")
                System("mountefi").boot()
                System("win").boot()
            }
            rec.setOnClickListener {
                hide("rec")
                System("rec").boot()
            }
            fbt.setOnClickListener {
                hide("fbt")
                System("fbt").boot()
            }
            dnx.setOnClickListener {
                hide("dnx")
                System("dnx").boot()
            }
            pwd.setOnClickListener {
                hide("pwd")
                System("pwd").boot()
            }
            sfm.setOnClickListener {
                hide("sfm")
                System("sfm").boot()
            }
            scn.setOnClickListener {
                System("scn").boot()
            }
        }
    }

    private fun hide(OS: String) {

        when (OS) {
            "and" -> {
                hideStack("2")
                hideStack("3")
                hideStack("4")
                hideStack("5")
                hideStack("6")
                hideStack("7")
                hideStack("8")
            }
            "win" -> {
                hideStack("1")
                hideStack("3")
                hideStack("4")
                hideStack("5")
                hideStack("6")
                hideStack("7")
                hideStack("8")
            }
            "rec" -> {
                hideStack("1")
                hideStack("2")
                hideStack("4")
                hideStack("5")
                hideStack("6")
                hideStack("7")
                hideStack("8")
            }
            "fbt" -> {
                hideStack("1")
                hideStack("2")
                hideStack("3")
                hideStack("5")
                hideStack("6")
                hideStack("7")
                hideStack("8")
            }
            "dnx" -> {
                hideStack("1")
                hideStack("2")
                hideStack("3")
                hideStack("4")
                hideStack("6")
                hideStack("7")
                hideStack("8")
            }
            "pwd" -> {
                hideStack("1")
                hideStack("2")
                hideStack("3")
                hideStack("4")
                hideStack("5")
                hideStack("7")
                hideStack("8")
            }
            "sfm" -> {
                hideStack("1")
                hideStack("2")
                hideStack("3")
                hideStack("4")
                hideStack("5")
                hideStack("6")
                hideStack("8")
            }
        }
    }

    private fun hideStack (choose: String) {

        val and = findViewById<ImageView>(R.id.imageAndroid)
        val andText = findViewById<TextView>(R.id.textAndroid)

        val win = findViewById<ImageView>(R.id.imageWindows)
        val winText = findViewById<TextView>(R.id.textWindows)

        val rec = findViewById<ImageView>(R.id.imageRecovery)
        val recText = findViewById<TextView>(R.id.textRecovery)

        val fbt = findViewById<ImageView>(R.id.imageBootloader)
        val fbtText = findViewById<TextView>(R.id.textBootloader)

        val dnx = findViewById<ImageView>(R.id.imageDNX)
        val dnxText = findViewById<TextView>(R.id.textDNX)

        val pwd = findViewById<ImageView>(R.id.imageShutdown)
        val pwdText = findViewById<TextView>(R.id.textPowerOff)

        val sfm = findViewById<ImageView>(R.id.imageSafeMode)
        val sfmText = findViewById<TextView>(R.id.textSafeMode)

        val scn = findViewById<ImageView>(R.id.imageScrnOff)
        val scnText = findViewById<TextView>(R.id.textScrnOff)

        when (choose) {
            "1" -> {
                and.visibility = View.INVISIBLE
                andText.visibility = View.INVISIBLE
            }
            "2" -> {
                win.visibility = View.INVISIBLE
                winText.visibility = View.INVISIBLE
            }
            "3" -> {
                rec.visibility = View.INVISIBLE
                recText.visibility = View.INVISIBLE
            }
            "4" -> {
                fbt.visibility = View.INVISIBLE
                fbtText.visibility = View.INVISIBLE
            }
            "5" -> {
                dnx.visibility = View.INVISIBLE
                dnxText.visibility = View.INVISIBLE
            }
            "6" -> {
                pwd.visibility = View.INVISIBLE
                pwdText.visibility = View.INVISIBLE

            }
            "7" -> {
                sfm.visibility = View.INVISIBLE
                sfmText.visibility = View.INVISIBLE

            }
            "8" -> {
                scn.visibility = View.INVISIBLE
                scnText.visibility = View.INVISIBLE

            }
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

        rcView?.layoutManager = GridLayoutManager(this@MainActivity, 3)
        rcView?.adapter = adapter
        val bootOptions = BootOptions(imageIdList[0], titleIdList[0].toString())
        adapter.addBootOptions(bootOptions)
    }
}