package com.enigma.application.presentation.mytask

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.enigma.application.R
import com.enigma.application.data.model.mytask.DataItem
import com.enigma.application.databinding.ContainerMyTaskBinding

class MyTaskViewHolder(view: View, val onClickListener: MyTaskOnClickListener) :
    RecyclerView.ViewHolder(view) {

    private val binding = ContainerMyTaskBinding.bind(view)
    fun bind(data: DataItem) {
        binding.apply {
            data?.apply {

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

                when (status) {
                    "PICKUP" -> {
                        labelStatus.setImageResource(R.drawable.ic_baseline_arrow_circle_up_24)
                        buttonDoneTask.visibility = View.VISIBLE
                        buttonCancelTask.visibility = View.GONE
                    }
                    "ASSIGNED" -> {
                        labelStatus.setImageResource(R.drawable.ic_baseline_label_24)
                        buttonCancelTask.visibility = View.VISIBLE
                        buttonDoneTask.visibility = View.GONE
                    }
                }

                tvAddress.text = this.destination?.address
                tvReceiver.text = this.destination?.name
                dateRequest.text = this.createDate?.substring(0, 10)
                nameUser.text =
                    "${this.requestBy?.userDetails?.firstName} ${this.requestBy?.userDetails?.lastName}"
                statusTask.text = this.status

                buttonCancelTask.setOnClickListener {
                    this.id?.let { id -> onClickListener.onClickUnAssign(id) }
                }

                buttonDoneTask.setOnClickListener {
                    this.id?.let { id -> onClickListener.onClickDone(id) }
                }

//                buttonAddTask.setOnClickListener {
//                    this.id?.let { id -> onClickListener.onClickAction(id) }
//                }
            }
        }
    }
}