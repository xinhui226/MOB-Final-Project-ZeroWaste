package com.xinhui.mobfinalproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.mobfinalproject.data.model.Notification
import com.xinhui.mobfinalproject.databinding.NotificationItemLayoutBinding

class NotificationAdapter(
    private var notification: List<Notification>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            NotificationItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return NotificationViewHolder(binding)
    }

    override fun getItemCount() = notification.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notif = notification[position]
        if (holder is NotificationViewHolder) {
            holder.bind(notif)
        }
    }

    fun showNotification(notif: List<Notification>) {
        this.notification = notif
        notifyDataSetChanged()
    }


    inner class NotificationViewHolder(
        private val binding: NotificationItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.run {
                tvProductName.text = notification.productName
                tvNotifyDateTime.text = notification.notifyDateTime
                tvExpireStatus.text = notification.expireStatus
                ivDelete.setOnClickListener {
                    listener?.onDelete(notification)
                }
            }
        }
    }

    interface Listener {
        fun onDelete(notification: Notification)
    }
}