package com.xinhui.mobfinalproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.R.color.design_default_color_error
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.data.model.Product
import com.xinhui.mobfinalproject.databinding.ShowItemLayoutBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class FoodItemAdapter(
    private var products: List<Product>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ShowItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodItemViewHolder(binding)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pro = products[position]
        if (holder is FoodItemViewHolder) {
            holder.bind(pro)
        }
    }

    fun showItems(pro: List<Product>) {
        this.products = pro
        notifyDataSetChanged()
    }

    inner class FoodItemViewHolder(
        private val binding: ShowItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.run {
                Glide.with(binding.root)
                    .load(product.productUrl)
                    .placeholder(R.drawable.chicken)
                    .into(binding.ivImage)
                tvFood.text = product.productName
                tvLocation.text = product.storagePlace
                tvCategory.text = product.category.categoryName

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val expiryDate = LocalDate.parse(product.expiryDate, formatter)
                val currDate = LocalDate.now()
                val daysUntilExpired = ChronoUnit.DAYS.between(currDate, expiryDate)

                setExpiredTextColor(binding.root.context, tvExpired, daysUntilExpired)

                ivDelete.setOnClickListener {
                    listener?.onDelete(product.id!!)
                }
                ivEdit.setOnClickListener {
                    listener?.onEdit(product.id!!)
                }
            }
        }
    }

    fun setExpiredTextColor(context: Context, tv: TextView, date: Long) {
        tv.setTextColor(
            context.getColor(if (date > 2) R.color.black else design_default_color_error))
        tv.text = when {
            date.toInt() == 1 -> context.getString(
                R.string.expiring_in_day, date.toString())
            date.toInt() == -1 -> context.getString(
                R.string.expired_day_ago, abs(date).toString())
            date > 0 ->  context.getString(
                R.string.expiring_in_days, date.toString())
            date < 0 -> context.getString(R.string.expired_days_ago,
                abs(date).toString())
            else -> context.getString(R.string.expired)
        }
    }

    interface Listener {
        fun onDelete(id: String)
        fun onEdit(id:String)
    }

}


