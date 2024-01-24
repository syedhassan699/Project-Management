package com.example.projectmanagement.adaptors

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.databinding.ItemLabelColorBinding

class LabelColorItemListAdaptor
    (private val context: Context,
    private val list:ArrayList<String>,
    private val mSelectedColor:String):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private class MyViewHolder(val binding: ItemLabelColorBinding):RecyclerView.ViewHolder(binding.root)
    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemLabelColorBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is MyViewHolder){
            holder.binding.viewMain.setBackgroundColor(Color.parseColor(item))
            if (item == mSelectedColor){
                holder.binding.ivSelectedColor.visibility = View.VISIBLE
            }else{
                holder.binding.ivSelectedColor.visibility = View.INVISIBLE
            }
            holder.itemView.setOnClickListener{
                if (onItemClickListener!=null){
                    onItemClickListener!!.onClick(position,item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int,color: String)
    }

}