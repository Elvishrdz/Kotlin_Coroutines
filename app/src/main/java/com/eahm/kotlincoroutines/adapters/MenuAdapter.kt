package com.eahm.kotlincoroutines.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.eahm.kotlincoroutines.R
import com.eahm.kotlincoroutines.models.MenuElement

class MenuAdapter(
    private val content : List<MenuElement>
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(){

    private lateinit var rootView : View

    inner class MenuViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val root : ConstraintLayout = view.findViewById(R.id.itemMenu)
        private val title : TextView = view.findViewById(R.id.tvMenuTitle)
        private val description : TextView = view.findViewById(R.id.tvMenuDescription)

        fun bind(element : MenuElement){
            title.text = element.title
            description.text = element.description
            root.setOnClickListener{
                rootView.context.startActivity(Intent(rootView.context, element.activity))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(rootView)
    }

    override fun getItemCount(): Int = content.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(content[position])
    }
}