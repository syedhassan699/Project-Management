package com.example.projectmanagement.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.adaptors.LabelColorItemListAdaptor
import com.example.projectmanagement.databinding.DialogListBinding

abstract class LabelColorListDialog
    (context: Context,
     private var list: ArrayList<String> = ArrayList(),
     private val title: String = "",
     private var mSelectedColor: String = ""):Dialog(context)
{
         private var adapter:LabelColorItemListAdaptor? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogListBinding.inflate(LayoutInflater.from(context))

        setContentView(binding.root)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(binding)
    }

    private fun setUpRecyclerView(binding: DialogListBinding){
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorItemListAdaptor(context,list,mSelectedColor)
        binding.rvList.adapter = adapter

        adapter!!.onItemClickListener = object : LabelColorItemListAdaptor.OnItemClickListener{
            override fun onClick(position: Int, color: String) {
                dismiss()
                onItemSelected(color)
            }

        }
    }

    protected abstract fun onItemSelected (color:String)

}