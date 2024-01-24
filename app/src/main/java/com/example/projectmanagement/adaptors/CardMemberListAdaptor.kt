package com.example.projectmanagement.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ItemCardSelectedMemberBinding
import com.example.projectmanagement.databinding.ItemLabelColorBinding
import com.example.projectmanagement.databinding.ItemMemberBinding
import com.example.projectmanagement.models.SelectedMembers

open class CardMemberListAdaptor(
    private val context: Context,
    private val list: ArrayList<SelectedMembers>,
    private val assignMember:Boolean
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private class MyViewHolder(val binding: ItemCardSelectedMemberBinding):RecyclerView.ViewHolder(binding.root)
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCardSelectedMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            if (position == list.size - 1 && assignMember){
                holder.binding.ivAddMember.visibility = View.VISIBLE
                holder.binding.ivSelectedMemberImage.visibility = View.GONE
            }else{
                holder.binding.ivAddMember.visibility = View.GONE
                holder.binding.ivSelectedMemberImage.visibility = View.VISIBLE
            }
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.binding.ivSelectedMemberImage)
        }
        holder.itemView.setOnClickListener {
            if (onClickListener!=null){
                onClickListener!!.onClick()
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    interface OnClickListener {
        fun onClick()
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
}
