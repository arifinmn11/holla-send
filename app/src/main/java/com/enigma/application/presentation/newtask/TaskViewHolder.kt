package com.enigma.application.presentation.newtask

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.enigma.application.R
import com.enigma.application.data.model.newtask.DataItem
import com.enigma.application.databinding.ContainerNewTaskBinding

class TaskViewHolder(
    view: View,
    val onClickListener: TaskOnClickListener
) :
    RecyclerView.ViewHolder(view) {

    private val binding = ContainerNewTaskBinding.bind(view)
    fun bind(data: DataItem) {
        binding.apply {
            data?.apply {
                tvAddress.text = this.destination?.address
                tvReceiver.text = this.destination?.name
                dateRequest.text = this.createDate?.substring(0, 10)
                nameUser.text =
                    "${this.requestBy?.userDetails?.firstName} ${this.requestBy?.userDetails?.lastName}"

                buttonAddTask.setOnClickListener {
                    this.id?.let { id -> onClickListener.onClick(id) }
                }

                when (priority) {
                    "HIGH" -> {
                        labelPriority.setImageResource(R.drawable.label_high_priority)
                    }
                    "MEDIUM" -> {
                        labelPriority.setImageResource(R.drawable.label_meidum_priority)
                    }
                    "LOW" -> {
                        labelPriority.setImageResource(R.drawable.label_low_priority)
                    }
                }
            }
        }
    }
}