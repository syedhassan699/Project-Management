package com.example.projectmanagement.adaptors

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.databinding.ItemTaskBinding
import com.example.projectmanagement.models.Task

open class TaskListItemAdaptor
    (private val context: Context,
     private var list: ArrayList<Task>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    class MyViewHolder(val binding:ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding= ItemTaskBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false)
        val layoutParams = LinearLayout.LayoutParams((parent.width * 0.7).toInt(),
        LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins((15.toDp().toPx()),0,(40.toDp().toPx()),0)
        binding.root.layoutParams = layoutParams
        return MyViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            if (position == list.size-1){
                holder.binding.tvAddTask.visibility = View.VISIBLE
                holder.binding.llTaskItem.visibility = View.GONE
            }else{
                holder.binding.tvAddTask.visibility = View.GONE
                holder.binding.llTaskItem.visibility = View.VISIBLE
            }

            holder.binding.tvTaskListTitle.text = model.title

            holder.binding.tvAddTask.setOnClickListener {
                holder.binding.tvAddTask.visibility = View.GONE
                holder.binding.cvAddTaskListName.visibility = View.VISIBLE
            }

            holder.binding.ibCloseListName.setOnClickListener {
                holder.binding.tvAddTask.visibility = View.VISIBLE
                holder.binding.cvAddTaskListName.visibility = View.GONE
            }

            holder.binding.ibDoneListName.setOnClickListener {
                // TODO Create Entry in DB and Display it Here
            }

        }
    }

    private fun Int.toDp():Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx():Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()

}