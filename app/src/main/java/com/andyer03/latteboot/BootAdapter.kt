package com.andyer03.latteboot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                        System("and").boot()
                    }
                    1 -> {
                        System("sfm").boot()
                    }
                    2 -> {
                        System("scn").boot()
                    }
                    3 -> {
                        System("rec").boot()
                    }
                    4 -> {
                        System("fbt").boot()
                    }
                    5 -> {
                        System("dnx").boot()
                    }
                    6 -> {
                        System("pwd").boot()
                    }
                    7 -> {
                        System("mountefi").boot()
                        System("win").boot()
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
        notifyDataSetChanged()
    }
}