package com.example.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.colosseum_20200716.R
import com.example.colosseum_20200716.datas.Reply


class ReplyAdapter(val mContext: Context, val resId:Int, val mList:List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList){

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if(tempRow == null){
            tempRow = inf.inflate(R.layout.reply_list_item, null)
        }
        val row = tempRow!!
        val writerNameTxt = row.findViewById<TextView>(R.id.writerTxt)
        val opinionTxt = row.findViewById<TextView>(R.id.opinionTxt)
        val selectedSideTxt = row.findViewById<TextView>(R.id.selectedSideTxt)
        val data = mList[position]

        writerNameTxt.text = data.writer.toString()
        opinionTxt.text = data.content
        selectedSideTxt.text = data.selectedSide.toString()

        return row
    }
}