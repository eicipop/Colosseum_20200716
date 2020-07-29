package com.example.colosseum_20200716.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.colosseum_20200716.R
import com.example.colosseum_20200716.ViewReplyDetailActivity
import com.example.colosseum_20200716.datas.Reply
import com.example.colosseum_20200716.utils.ServerUtil
import com.example.colosseum_20200716.utils.TimeUtil
import org.json.JSONObject
import java.text.SimpleDateFormat


class ReReplyAdapter(val mContext: Context, val resId:Int, val mList:List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList){

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if(tempRow == null){
            tempRow = inf.inflate(R.layout.re_reply_list_item, null)
        }
        val row = tempRow!!
        val writerNickNameTxt = row.findViewById<TextView>(R.id.writerNickNameTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)
        // 시간정보 텍스트뷰
        var replywriteTimeTxt = row.findViewById<TextView>(R.id.replyWriteTimeTxt)
        //댓/좋/실 버튼 추가

        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val dislikeBtn = row.findViewById<Button>(R.id.dislikeBtn)


        val data = mList[position]

        writerNickNameTxt.text = data.writer.nickName

        selectedSideTitleTxt.text = "(${data.selectedSide.title})"

        replywriteTimeTxt.text = TimeUtil.getTimeAgoFromCalendar(data.writtenDateTime)

        contentTxt.text = data.content

        likeBtn.text = "좋아요 ${data.likecount}"
        dislikeBtn.text = "싫어요 ${data.dislikecount}"

//        좋아요 여부에 따른 색 / 싫어요 여부에 따른 색 설정
        if (data.myLike) {
            likeBtn.setBackgroundResource(R.drawable.red_border_box)
            likeBtn.setTextColor(mContext.resources.getColor(R.color.red))
        }
        else {
            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
            likeBtn.setTextColor(mContext.resources.getColor(R.color.textGray))
        }

        if (data.myDislike) {
            dislikeBtn.setBackgroundResource(R.drawable.blue_border_box)
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.blue))
        }
        else {
            dislikeBtn.setBackgroundResource(R.drawable.gray_border_box)
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.textGray))
        }

//        좋아요 / 싫어요 모두 실행하는 코드는 동일함.
//         서버에 true / false 를 보내는지, 보내주는 값만 다를 뿐.
//        두개의 버튼이 눌리면 할 일 (object : ??)을 => 변수에 담아두고, 버튼에게 붙여만 주자.

        val sendLikeOrDislikeCode = View.OnClickListener {

//            서버에 좋아요 /싫어요 중 하나를 보내주자.
//            it 달린 태그값을 => Boolean으로 변환해서 좋아요/싫어요를 구별하자.
//            태그 -> String -> Boolean 의 단계로 변환해야함.
            ServerUtil.postRequestReplyLikeOrDislike(mContext, data.id, it.tag.toString().toBoolean(), object : ServerUtil.JsonResponseHandler {
                override fun onResponse(json: JSONObject) {

                    val dataObj = json.getJSONObject("data")

                    val reply = Reply.getReplyFromJson(dataObj.getJSONObject("reply"))

                    data.likecount = reply.likecount
                    data.dislikecount = reply.dislikecount
                    data.myLike = reply.myLike
                    data.myDislike = reply.myDislike

                    val uiHandler = Handler(Looper.getMainLooper())

                    uiHandler.post {
                        notifyDataSetChanged()
                    }

                }

            })

        }

//        좋아요버튼 / 싫어요버튼이 클릭되면 => sendLikeOrDislikeCode 내부의 내용을 실행하게 하자.
        likeBtn.setOnClickListener(sendLikeOrDislikeCode)
        dislikeBtn.setOnClickListener(sendLikeOrDislikeCode)


        return row
    }

}