package com.example.colosseum_20200716.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.colosseum_20200716.R
import com.example.colosseum_20200716.ViewReplyDetailActivity
import com.example.colosseum_20200716.datas.Reply
import com.example.colosseum_20200716.utils.TimeUtil
import java.text.SimpleDateFormat


class ReReplyAdapter(val mContext: Context, val resId:Int, val mList:List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList){

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if(tempRow == null){
            tempRow = inf.inflate(R.layout.re_reply_list_item, null)
        }
        val row = tempRow!!
        return row
    }
}