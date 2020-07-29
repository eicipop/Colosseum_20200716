package com.example.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colosseum_20200716.adapters.NotificationAdapter
import com.example.colosseum_20200716.adapters.ReReplyAdapter
import com.example.colosseum_20200716.datas.Notification
import com.example.colosseum_20200716.datas.Reply
import com.example.colosseum_20200716.utils.ServerUtil
import com.example.colosseum_20200716.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import kotlinx.android.synthetic.main.notification_list_item.*
import org.json.JSONObject

class NotificationListActivity : BaseActivity() {
    var mNotifyId = 0
    lateinit var mNotification : Notification

    val mNotifyList = ArrayList<Notification>()
    lateinit var mNotificationAdapter : NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        setupEvents()
        setValues()
    }
    override fun setupEvents() {


    }

    override fun setValues() {
        //의견 리스트뷰에서 보내준 id값을 멤버변수에 담아주자.
        mNotifyId = intent.getIntExtra("id", 0)
        getNotiListFromServer()
//        해당 id값에 맞는 의견 정보를 (서버에서) 다시 불러오자

        mNotificationAdapter = NotificationAdapter(mContext, R.layout.notification_list_item, mNotifyList)
        notiListView.adapter = mNotificationAdapter
    }


    fun getNotiListFromServer(){
        ServerUtil.getRequestNotificationList(mContext, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                val data = json.getJSONObject("data")
                val notifications = data.getJSONArray("notifications")

                for(i in 0 until notifications.length()){
 //                  JSONArray 내부의 JSONObject추출 => Notification 가공 -> mNotiList 에 담자.
                    mNotifyList.add(Notification.getNotificationFromJson(notifications.getJSONObject(i)))
                }

                runOnUiThread{




//                    답글 목록이 모두 불러지면 새로 반영
                    mNotificationAdapter.notifyDataSetChanged()

//                    리스트뷰의 맨 밑 (마지막 답글)으로 끌어내리기
//                    마지막 답글 : 목록의 맨 끝 => 목록의 길이 - 1 번째
//                    답글 10개 : 9번 마지막

                    notiListView.smoothScrollToPosition(mNotifyList.size - 1)
                }
            }

        })
    }
}