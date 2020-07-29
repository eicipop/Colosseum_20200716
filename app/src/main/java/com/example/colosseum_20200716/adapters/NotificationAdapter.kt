package com.example.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.colosseum_20200716.R
import com.example.colosseum_20200716.datas.Notification
import com.example.colosseum_20200716.datas.Reply
import com.example.colosseum_20200716.utils.TimeUtil

class NotificationAdapter(val mContext: Context, val resId:Int, val mList:List<Notification>) : ArrayAdapter<Notification>(mContext, resId, mList){
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if(tempRow == null){
            tempRow = inf.inflate(R.layout.notification_list_item, null)
        }
        val row = tempRow!!
        val notiTxt = row.findViewById<TextView>(R.id.notiTxt)
        // 시간정보 텍스트뷰
        val notiCreatedAtTxt = row.findViewById<TextView>(R.id.notiCreatedAtTxt)

        val data = mList[position]

        notiTxt.text = data.title
        notiCreatedAtTxt.text = TimeUtil.getTimeAgoFromCalendar((data.createAtCal))

        return row
    }
}