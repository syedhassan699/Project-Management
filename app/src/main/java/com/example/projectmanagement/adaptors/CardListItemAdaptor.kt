package com.example.projectmanagement.adaptors

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.databinding.ItemCardBinding
import com.example.projectmanagement.models.Card


open class CardListItemAdaptor  (private val context: Context,
                                 private var list: ArrayList<Card>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    class MyViewHolder(val binding: ItemCardBinding ) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding=ItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            if (model.labelColor.isNotEmpty()){
                holder.binding.viewLabelColor.visibility = View.VISIBLE
                holder.binding.viewLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
            }else{
                holder.binding.viewLabelColor.visibility = View.GONE
            }

            holder.binding.tvCardName.text = model.name
            holder.itemView.setOnClickListener{
                if (onClickListener != null) {
                    onClickListener!!.onClick(position)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(cardPosition: Int)
    }
}