package com.enigma.application.presentation.newtask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enigma.application.R
import com.enigma.application.data.model.newtask.DataItem

class TaskAdapter(val onClickListener: TaskOnClickListener) :
    RecyclerView.Adapter<TaskViewHolder>() {
    var tasks = ArrayList<DataItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.container_new_task, parent, false)
        return TaskViewHolder(v, onClickListener)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

    }

    fun setView(data: List<DataItem>) {
        tasks.clear()
        tasks.addAll(data)
        notifyDataSetChanged()
    }
}