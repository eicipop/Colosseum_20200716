package com.example.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.colosseum_20200716.adapters.TopicAdapter
import com.example.colosseum_20200716.datas.Topic
import com.example.colosseum_20200716.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<Topic>()

    lateinit var mTopicAdapter: TopicAdapter
    override fun setupEvents() {

        // 알림버튼을 누르면 알림목록 화면으로 이동
        noticationBtn.setOnClickListener {
        // 이미지뷰도 setOnClickListener 설정 가능.
            val myIntent = Intent(mContext, NotificationListActivity::class.java)
            startActivity(myIntent)
        }
//  토론 주제를 누르면 상세화면으로 이동
        topicListView.setOnItemClickListener { parent, view, position, id ->
            // 눌린 위치에 맞는 주제를 가져오자
            val clickedTopic = mTopicList[position]

            // 상세화면 진입 => 클릭된 주제의 id값만 화면에 전달

            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topicId", clickedTopic.id)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        getTopicListFromServer()

        mTopicAdapter = TopicAdapter(mContext, R.layout.topic_list_item, mTopicList)
        topicListView.adapter = mTopicAdapter

//  BaseActivity가 물려주는 알림버튼을 화면에 보이도록
        noticationBtn.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
    // 메인 화면에 돌아올 때마다 서버에 안읽은 알림이 몇개인지 요청
        getNoticountFromServer()
    }

    // 안 읽은 알림갯수만 가져오는 API호출
    fun getNoticountFromServer(){
        ServerUtil.getRequestNotificationCount(mContext, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                val data = json.getJSONObject("data")
                val unreadNotiCount = data.getInt("unread_noty_count")
//              안 읽은 알림이 몇개인지 에 따라다른 UI처리

                runOnUiThread {
                    if (unreadNotiCount == 0){
    //                  빨간 동그라미 숨겨주기
                        notiCountTxt.visibility = View.GONE
                    }else{
                    // 빨간 동그라미 표시 + 갯수반영
                        notiCountTxt.visibility = View.VISIBLE
                        notiCountTxt.text = unreadNotiCount.toString()
                    }
                }
            }

        })
    }
    fun getTopicListFromServer(){
        ServerUtil.getRequestMainInfo(mContext, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                val data = json.getJSONObject("data")
            //topics는  [ ] 임 => JSONArray 로 추출해야함
                val topics = data.getJSONArray("topics")
            //topics 내부에는 JSONObject가 여러개 반복으로 들어있따.
                    //JSON을 들고 있는 배열 => JSONArray
                //for문을 이용해서 topics내부의 데이터를 하나씩 추출
                    //i가 0부터 ~topics 의 갯수 직전.4개 : (0,1,2,3)
                for(i in 0 until topics.length()){
                    //topics 내부의 데이터를 JSONObject로 추출
                    val topicObj = topics.getJSONObject(i)
                    //topicobj = >Topic 형태의 객체로 변환
                    val topic = Topic.getTopicFromJson(topicObj)
                    // 변환된 객체를 목록에 추가
                    mTopicList.add(topic)
                }
                //for문으로 주제 목록을 모두 추가하고 나면
                //리스트뷰의 내용이 바꼈다고 새로고침
                runOnUiThread {
                    mTopicAdapter.notifyDataSetChanged()
                }
           }

        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setValues()
        setupEvents()
    }
}