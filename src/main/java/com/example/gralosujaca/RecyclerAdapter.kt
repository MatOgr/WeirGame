package com.example.gralosujaca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val scorelist: List<String>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    private var scoreList : MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.MyViewHolder, position: Int) {
        holder.itemText.text = scorelist[position]
//        holder.itemImage.setImageResource(images_list[position])
    }

    override fun getItemCount() = scorelist.size


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById<TextView>(R.id.itemScoreText)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                Toast.makeText(
                    itemView.context,
                    "You clicked ${scoreList[position]}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}