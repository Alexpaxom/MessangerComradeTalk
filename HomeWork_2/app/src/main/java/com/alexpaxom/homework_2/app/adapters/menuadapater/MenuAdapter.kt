package com.alexpaxom.homework_2.app.adapters.menuadapater

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.alexpaxom.homework_2.databinding.DefaultMenuItemBinding

class MenuAdapter(
    private val menu: Menu,
    private val onMenuItemClickListener: (selectedMenuItem: MenuItem) -> Unit
): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val holder = MenuViewHolder(
            binding = DefaultMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onMenuItemClickListener = onMenuItemClickListener
        )

        return holder
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menu.getItem(position))
    }

    override fun getItemCount(): Int = menu.size

    inner class MenuViewHolder(
        private val binding: DefaultMenuItemBinding,
        private val onMenuItemClickListener: (selectedMenuItem: MenuItem) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.menuItem.setOnClickListener {
                onMenuItemClickListener(menu.getItem(absoluteAdapterPosition))
            }
        }

        fun bind(menuItem: MenuItem) {
            binding.menuTitle.text = menuItem.title
        }
    }
}