package com.example.projectmanagement.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanagement.activtities.TaskListActivity
import com.example.projectmanagement.databinding.ItemCardBinding
import com.example.projectmanagement.models.Card
import com.example.projectmanagement.models.SelectedMembers


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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            if (model.labelColor.isNotEmpty()){
                holder.binding.viewLabelColor.visibility = View.VISIBLE
                holder.binding.viewLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
            }else{
                holder.binding.viewLabelColor.visibility = View.GONE
            }

            holder.binding.tvCardName.text = model.name

            if ((context as TaskListActivity).mAssignedMemberDetailList.size>0){
                val selectedMemberList:ArrayList<SelectedMembers> = ArrayList()
                for (i in context.mAssignedMemberDetailList.indices){
                    for (j in model.assignedTo){
                        if (context.mAssignedMemberDetailList[i].id == j){
                            val selectedMembers = SelectedMembers(context
                                .mAssignedMemberDetailList[i].id,
                                context.mAssignedMemberDetailList[i].image
                            )
                            selectedMemberList.add(selectedMembers)
                        }
                    }
                }
                if (selectedMemberList.size > 0){
                     if (selectedMemberList.size == 1 && selectedMemberList[0].id == model.createdBy){
                        holder.binding.rvCardSelectedMembersList.visibility = View.GONE
                     }else{
                         holder.binding.rvCardSelectedMembersList.visibility = View.VISIBLE
                         holder.binding.rvCardSelectedMembersList.layoutManager=
                             GridLayoutManager(context,4)

                         val adaptor = CardMemberListAdaptor(context,selectedMemberList,false)
                         holder.binding.rvCardSelectedMembersList.adapter = adaptor
                         adaptor.setOnClickListener(object
                             : CardMemberListAdaptor.OnClickListener{
                             override fun onClick() {
                                 if (onClickListener != null){
                                     onClickListener!!.onClick(position)
                                 }
                             }

                         })
                     }
                }else{
                    holder.binding.rvCardSelectedMembersList.visibility = View.GONE
                }
            }

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