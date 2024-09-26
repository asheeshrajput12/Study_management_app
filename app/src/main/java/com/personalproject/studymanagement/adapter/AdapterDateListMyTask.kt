package com.personalproject.studymanagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.personalproject.studymanagement.databinding.ItemTaskBinding

class AdapterDateListMyTask(val list: List<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ItemTaskBinding=ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(ItemTaskBinding)

    }

    override fun getItemCount(): Int {
       return 10;
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
    inner class MyViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)
}