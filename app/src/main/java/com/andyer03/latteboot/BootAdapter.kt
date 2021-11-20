package com.andyer03.latteboot

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andyer03.latteboot.commands.*
import com.andyer03.latteboot.databinding.BootItemBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class BootAdapter: RecyclerView.Adapter<BootAdapter.BootHolder>() {
    private val bootList = ArrayList<BootOptions>()

    class BootHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = BootItemBinding.bind(item)
        private val handler = Handler()
        fun bind(bootOptions: BootOptions) = with(binding) {
            icon.setImageResource(bootOptions.imageId)
            title.text = bootOptions.title
            currentBootloader.text = bootOptions.current
        }

        init {
            itemView.setOnClickListener {
                // Delay command execution for abort possibility
                val scn = Runnable {
                    System("scn").boot()
                }
                val pwd = Runnable {
                    System("pwd").boot()
                }
                val rec = Runnable {
                    System("rec").boot()
                }
                val fbt = Runnable {
                    System("fbt").boot()
                }
                val dnx = Runnable {
                    System("dnx").boot()
                }
                val sfm = Runnable {
                    System("sfm").boot()
                }
                val reboot = Runnable {
                    System("reboot").boot()
                }
                val and = Runnable {
                    when {
                        BootFile().check() == "Android" -> {
                            BootAnotherOS().android()
                        }
                    }
                }
                val win = Runnable {
                    when {
                        BootFile().check() == "Windows" -> {
                            BootAnotherOS().windows()
                        }
                    }
                }

                val context = it
                val duration = context.resources.getString(R.string.after_5_seconds_title) // Duration
                val cancel = context.resources.getString(R.string.cancel_title) // Cancel
                val abort = context.resources.getString(R.string.action_aborted)

                val optionsTitles = arrayOf (
                    context.resources.getString(R.string.reboot_screen_off_title), // 0 Screen off
                    context.resources.getString(R.string.reboot_shutdown_title), // 1 Shutdown
                    context.resources.getString(R.string.reboot_recovery_title), // 2 Recovery
                    context.resources.getString(R.string.reboot_bootloader_title), // 3 Bootloader
                    context.resources.getString(R.string.reboot_dnx_title), // 4 DNX
                    context.resources.getString(R.string.reboot_safe_mode_title), // 5 Safe mode
                    context.resources.getString(R.string.reboot_device_title), // 6 Reboot
                    context.resources.getString(R.string.reboot_android_title), // 7 Android
                    context.resources.getString(R.string.reboot_windows_title), // 8 Windows
                )

                // Cancel all delays befor executing one command to have no issue
                handler.removeCallbacksAndMessages(null)
                when (adapterPosition) {
                    0 -> {
                        handler.postDelayed(scn, 5000)
                        snack(optionsTitles[0], duration, cancel, abort)
                    }
                    1 -> {
                        handler.postDelayed(pwd, 5000)
                        snack(optionsTitles[1], duration, cancel, abort)
                    }
                    2 -> {
                        handler.postDelayed(rec, 5000)
                        snack(optionsTitles[2], duration, cancel, abort)
                    }
                    3 -> {
                        handler.postDelayed(fbt, 5000)
                        snack(optionsTitles[3], duration, cancel, abort)
                    }
                    4 -> {
                        handler.postDelayed(dnx, 5000)
                        snack(optionsTitles[4], duration, cancel, abort)
                    }
                    5 -> {
                        handler.postDelayed(sfm, 5000)
                        snack(optionsTitles[5], duration, cancel, abort)
                    }
                    6 -> {
                        handler.postDelayed(reboot, 5000)
                        snack(optionsTitles[6], duration, cancel, abort)
                    }
                    7 -> {
                        handler.postDelayed(and, 5000)
                        snack(optionsTitles[7], duration, cancel, abort)
                    }
                    8 -> {
                        handler.postDelayed(win, 5000)
                        snack(optionsTitles[8], duration, cancel, abort)
                    }
                }
            }
        }

        // Show snack bar
        private fun snack(title: String, duration: String, cancel: String, abort: String) {
            val snackbar = Snackbar.make(
                binding.root,
                "$title $duration",
                4500,
            ).setAction(cancel) {
                handler.removeCallbacksAndMessages(null)
                abortSnack(abort)
            }
            snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            snackbar.show()
        }

        // Show abort snack bar
        private fun abortSnack(abort: String) {
            val abortSnackBar = Snackbar.make(
                binding.root,
                abort,
                Snackbar.LENGTH_LONG,
            )
            abortSnackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            abortSnackBar.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BootHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.boot_item, parent, false)
        return BootHolder(view)
    }

    override fun onBindViewHolder(holder: BootHolder, position: Int) {
        holder.bind(bootList[position])
    }

    override fun getItemCount(): Int {
        return bootList.size
    }

    fun addBootOptions(bootOptions: BootOptions) {
        bootList.add(bootOptions)
    }

    fun clear() {
        bootList.clear()
    }
}