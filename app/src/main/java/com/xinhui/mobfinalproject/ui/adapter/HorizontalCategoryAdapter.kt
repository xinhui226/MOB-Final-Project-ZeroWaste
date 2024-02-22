package com.xinhui.mobfinalproject.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xinhui.mobfinalproject.core.utils.Category
import com.xinhui.mobfinalproject.databinding.HorizontalItemsBinding

class HorizontalCategoryAdapter(
    private var selectedCategory: String,
    private var onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<HorizontalCategoryAdapter.CategoryClickViewHolder>() {

    private var categories: Array<Category> = Category.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalCategoryAdapter.CategoryClickViewHolder {
        val binding = HorizontalItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryClickViewHolder(binding)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryClickViewHolder, position: Int) {
        val items = categories[position]
        holder.bind(items)
    }

    fun setSelected(cat: String) {
        selectedCategory = cat
        notifyDataSetChanged()
    }

    inner class CategoryClickViewHolder(
        private val binding: HorizontalItemsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.run {

                if (category.categoryName == selectedCategory) {
                    tvCategories.setBackgroundColor(Color.parseColor("#3F72AF"))
                    tvCategories.setTextColor(Color.WHITE)
                    tvCategories.text = category.categoryName
                } else {
                    tvCategories.setBackgroundColor(Color.parseColor("#d9d9d9"))
                    tvCategories.setTextColor(Color.BLACK)
                    tvCategories.text = category.categoryName
                }

                itemView.setOnClickListener {
                    onCategoryClick(category)
                    setSelected(category.categoryName)
                }
            }
        }
    }

}