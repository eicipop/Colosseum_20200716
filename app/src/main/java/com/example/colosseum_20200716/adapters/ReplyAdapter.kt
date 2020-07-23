package com.example.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.colosseum_20200716.R
import com.example.colosseum_20200716.datas.Reply
import com.example.colosseum_20200716.utils.TimeUtil
import java.text.SimpleDateFormat


class ReplyAdapter(
    val mContext:Context,
    resId: Int,
    val mList:List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView

        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickNameTxt)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)

//        시간 정보 텍스트뷰
        val replyWriteTimeTxt = row.findViewById<TextView>(R.id.replayWriteTimeTxt)

        val data = mList[position]

        writerNickNameTxt.text = data.writer.nickName
        selectedSideTitleTxt.text = "(${data.selectedSide.title})"
        contentTxt.text = data.content

//        시간정보 텍스트뷰 내용 설정 => 방금 전, ?분 전, ?시간 전 등등..

        replyWriteTimeTxt.text = TimeUtil.getTimeAgoFromCalendar(data.writtenDateTime)


//        날짜 출력 양식용 변수
//        val sdf = SimpleDateFormat("yy-MM-dd a h시 m분")
//
//        replyWriteTimeTxt.text = sdf.format(data.writtenDateTime.time)


        return row
    }

}