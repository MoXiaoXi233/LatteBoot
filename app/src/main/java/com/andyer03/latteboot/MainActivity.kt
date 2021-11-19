package com.andyer03.latteboot

import android.annotation.SuppressLint
import android.content.*
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.andyer03.latteboot.databinding.ActivityMainBinding
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.andyer03.latteboot.*
import com.andyer03.latteboot.commands.*
import com.andyer03.latteboot.other.Arrays
import com.andyer03.latteboot.other.Device
import com.andyer03.latteboot.shortcuts.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.*

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = BootAdapter()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Device().check()) {
            Toast.makeText(this, R.string.mi_pad_2_toast, Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            val label = findViewById<TextView>(R.id.label)
            label.text = getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME + " " + getString(R.string.about_title)

            // Load the main code
            init()
        }
    }

    private fun init() = with(binding) {
        val orientation = resources.configuration.orientation
        val spanCount: Int = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        rcView.layoutManager = GridLayoutManager(this@MainActivity, spanCount)
        rcView.adapter = adapter

        // Filling adapter
        adapter.addBootOptions(Arrays().bootOptions[0]) // Screen off
        adapter.addBootOptions(Arrays().bootOptions[1]) // Shutdown
        adapter.addBootOptions(Arrays().bootOptions[2]) // Recovery
        adapter.addBootOptions(Arrays().bootOptions[3]) // Bootloader
        adapter.addBootOptions(Arrays().bootOptions[4]) // DNX
        adapter.addBootOptions(Arrays().bootOptions[5]) // Safemode

        when (Root().check()) {
            true -> {
                when (BootFile().check()) {
                    "Windows" -> {
                        adapter.addBootOptions(Arrays().bootOptions[7]) // Android
                        adapter.addBootOptions(Arrays().bootOptions[10]) // Current bootloader Windows
                    }
                    "Android" -> {
                        adapter.addBootOptions(Arrays().bootOptions[9]) // Current bootloader Android
                        adapter.addBootOptions(Arrays().bootOptions[8]) //
                    }
                    "Error" -> {
                        // None
                    }
                }
            }
            else -> {
                adapter.addBootOptions(Arrays().bootOptions[6]) // Simple reboot
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val p = packageManager
        val sp = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val spEditor: SharedPreferences.Editor = sp.edit()

        val preferences = arrayOf (
            ComponentName(applicationContext, RebootRecovery::class.java),  // 0 Recovery
            ComponentName(applicationContext, RebootWindows::class.java),   // 1 Windows
        )

        when (item.itemId) {
            // Show or hide shortcuts from app drawer
            R.id.bootloader_swap -> {
                when {
                    Root().check() -> {
                        LatteSwapBoot().swap() // Swap boot files
                        adapter.clear() // Clear adapter
                        init() // Recreating adapter
                        swapMessage() // Show toast after swap
                    }
                    else -> {
                        Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.recovery_shortcut -> {
                when (sp.getBoolean("recovery_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[0],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("recovery_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[0],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("recovery_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.windows_shortcut -> {
                when (sp.getBoolean("windows_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[1],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("windows_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[1],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("windows_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Parameters for Snack Bar
    fun swapMessage() {
        when {
            BootFile().check() == "Windows" -> {
                onSNACK(getString(R.string.reboot_windows_title))
            }
            BootFile().check() == "Android" -> {
                onSNACK(getString(R.string.reboot_android_title))
            }
            BootFile().check() == "Error" -> {
                onSNACK(getString(R.string.error_title))
            }
        }
    }

    // Snack Bar
    private fun onSNACK(text: String) {
        val bootItem = getString(R.string.boot_item_current)
        val snackbar = Snackbar.make(
            binding.root,
            "$bootItem: $text",
            Snackbar.LENGTH_LONG,
        )
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        snackbar.show()
    }
}