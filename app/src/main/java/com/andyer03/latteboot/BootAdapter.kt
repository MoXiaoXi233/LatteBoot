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