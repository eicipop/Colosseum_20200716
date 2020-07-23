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


class ReplyAdapter(val mContext: Context, val resId:Int, val mList:List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList){

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if(tempRow == null){
            tempRow = inf.inflate(R.layout.reply_list_item, null)
        }
        val row = tempRow!!
        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickNameTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)
        // 시간정보 텍스트뷰
        var replywriteTimeTxt = row.findViewById<TextView>(R.id.replyWriteTimeTxt)
        //댓/좋/실 버튼 추가
        val replyBtn = row.findViewById<Button>(R.id.replyBtn)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val dislikeBtn = row.findViewById<Button>(R.id.dislikeBtn)
        val data = mList[position]

        writerNickNameTxt.text = data.writer.nickName
        contentTxt.text = data.content
        selectedSideTitleTxt.text = "(${data.selectedSide.title})"

        // 시간정보 텍스트뷰 내용 설정 -> 방금전, ?분전, ?시간 전 등등..
        replywriteTimeTxt.text = TimeUtil.getTimeAgoFromCalendar(data.writtenDateTime)

        // 날짜 출력 양식용 변수
//        val sdf = SimpleDateFormat("yy-MM-dd a h시 m분")
//        replywriteTimeTxt.text = sdf.format(data.writtenDateTime.time)
        // 좋 /싫 / 답글 갯수 반영
        likeBtn.text = "좋아요 ${data.likecount}"
        dislikeBtn.text = "싫어요 ${data.dislikecount}"
        replyBtn.text = "답글 ${data.replyCount}"

        //답글 버튼이 눌리면 의견 상세 화면으로 진입
        replyBtn.setOnClickListener {

            val myIntent = Intent(mContext, ViewReplyDetailActivity::class.java)
            // startActivity 함수는 AppCompatActivity 가 내려주는 기능.
            // Adapter는 액티비티가 아니므로, startActivity 기능을 내려주지 않는다.
            // mContext 변수가, 어떤 화면이 리스트뷰를 뿌리는지 들고 있다.
            // mContext를 이용해서 액티비티를 열어주자.
            mContext.startActivity(myIntent)
        }
        return row
    }
}