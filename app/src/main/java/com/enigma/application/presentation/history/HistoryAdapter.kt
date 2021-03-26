package com.enigma.application.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enigma.application.R
import com.enigma.application.data.model.history.ListItem

class HistoryAdapter(val onClickListener: HistoryOnClickListener) :
    RecyclerView.Adapter<HistoryViewHolder>() {
    var tasks = ArrayList<ListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.container_history, parent, false)
        return HistoryViewHolder(v, onClickListener)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    fun setView(data: List<ListItem>) {
        tasks.clear()
        tasks.addAll(data)
        notifyDataSetChanged()
    }
}
