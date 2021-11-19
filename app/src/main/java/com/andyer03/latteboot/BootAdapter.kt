package com.andyer03.latteboot

import android.content.res.Resources
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andyer03.latteboot.commands.*
import com.andyer03.latteboot.databinding.BootItemBinding
import com.andyer03.latteboot.other.Arrays
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

                when (adapterPosition) {
                    0 -> {
                        handler.postDelayed(scn, 5000)
                        snack(Arrays().optionsTitles[0], scn)
                    }
                    1 -> {
                        handler.postDelayed(pwd, 5000)
                        snack(Arrays().optionsTitles[1], scn)
                    }
                    2 -> {
                        handler.postDelayed(rec, 5000)
                        snack(Arrays().optionsTitles[2], scn)
                    }
                    3 -> {
                        handler.postDelayed(fbt, 5000)
                        snack(Arrays().optionsTitles[3], scn)
                    }
                    4 -> {
                        handler.postDelayed(dnx, 5000)
                        snack(Arrays().optionsTitles[4], scn)
                    }
                    5 -> {
                        handler.postDelayed(sfm, 5000)
                        snack(Arrays().optionsTitles[5], scn)
                    }
                    6 -> {
                        handler.postDelayed(and, 5000)
                        snack(Arrays().optionsTitles[6], scn)
                    }
                    7 -> {
                        handler.postDelayed(win, 5000)
                        snack(Arrays().optionsTitles[7], scn)
                    }
                }
            }
        }

        private fun snack(title: String, option: Runnable) {
            val afterTitle = Resources.getSystem().getString(R.string.after_title)
            val secondsTitle = Resources.getSystem().getString(R.string.seconds_title)

            val snack = Snackbar.make(
                binding.root,
                "$title $afterTitle 5 $secondsTitle",
                4500,
            ).setAction("Cancel") {
                handler.removeCallbacks(option)
            }
            snack.show()
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