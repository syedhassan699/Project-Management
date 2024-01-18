package com.example.projectmanagement.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ItemBoardBinding
import com.example.projectmanagement.models.Board

open class BoardItemAdaptor (val context: Context,
                             private var list: ArrayList<Board>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener:OnClickListener?=null
    class MyViewHolder(binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root) {
        var ivBoardImage = binding.ivBoardImage
        var tvName = binding.tvName
        var tvCreatedBy = binding.tvCreatedBy
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.ivBoardImage)
            holder.tvName.text=model.name
            holder.tvCreatedBy.text="Created by : ${model.createdBy}"
            holder.itemView.setOnClickListener{
                if (onClickListener !=null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }
    interface OnClickListener{
        fun onClick(position: Int,model: Board)
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener = onClickListener
    }
}