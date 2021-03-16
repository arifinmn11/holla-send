package com.enigma.application.presentation.mytask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enigma.application.R
import com.enigma.application.data.model.mytask.DataItem

class MyTaskAdapter(val onClickListener: MyTaskOnClickListener) :
    RecyclerView.Adapter<MyTaskViewHolder>() {
    var tasks = ArrayList<DataItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.container_my_task, parent, false)
        return MyTaskViewHolder(v, onClickListener)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

    }

    fun setView(data: List<DataItem>) {
        tasks.clear()
        tasks.addAll(data)
        notifyDataSetChanged()
    }
}