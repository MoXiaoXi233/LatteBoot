package com.andyer03.latteboot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andyer03.latteboot.commands.*
import com.andyer03.latteboot.databinding.BootItemBinding

class BootAdapter: RecyclerView.Adapter<BootAdapter.BootHolder>() {
    private val bootList = ArrayList<BootOptions>()

    class BootHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = BootItemBinding.bind(item)
        fun bind(bootOptions: BootOptions) = with(binding) {
            icon.setImageResource(bootOptions.imageId)
            title.text = bootOptions.title
        }

        init {
            itemView.setOnClickListener {
                when (adapterPosition) {
                    0 -> {
                        System("scn").boot()
                    }
                    1 -> {
                        System("pwd").boot()
                    }
                    2 -> {
                        System("rec").boot()
                    }
                    3 -> {
                        System("fbt").boot()
                    }
                    4 -> {
                        System("dnx").boot()
                    }
                    5 -> {
                        System("sfm").boot()
                    }
                    6 -> {
                        System("reboot").boot()
                    }
                    7 -> {
                        when {
                            BootFile().check() == "Windows" -> {
                                RebootWindowsCom().execute()
                            }
                            BootFile().check() == "Android" -> {
                                RebootAndroidCom().execute()
                            }
                            else -> {
                                return@setOnClickListener
                            }
                        }
                    }
                }
            }
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