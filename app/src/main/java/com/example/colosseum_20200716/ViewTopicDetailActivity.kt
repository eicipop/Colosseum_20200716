package com.example.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.colosseum_20200716.adapters.ReplyAdapter
import com.example.colosseum_20200716.datas.Reply
import com.example.colosseum_20200716.datas.Topic
import com.example.colosseum_20200716.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import kotlinx.android.synthetic.main.reply_list_item.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    //    메인화면에서 넘겨준 주제 id 저장
    var mTopicId = 0 // 일단 Int임을 암시

    //    서버에서 받아오는 토론 정보를 저장할 멤버변수
    lateinit var mTopic : Topic
    // 토론 주제를 받아올때 딸려오는 의견목록을 담아줄 배열
    val mReplyList = ArrayList<Reply>()

    // 실제로 목록을 뿌려줄 어댑터
    lateinit var mReplyAdapter : ReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        getTopicDetailFromServer()
    }

    override fun setupEvents() {
//  의견등록하기 누르면 작성 화면으로
        postReplyBtn.setOnClickListener {
            // 투표하기 안한 상태라면, 작성화면 진입 거부(투표부터 시키자)
            if(mTopic.mySideId == -1){
                Toast.makeText(mContext, "투표를 해야만 의견을 작성할 수 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val myIntent = Intent(mContext, EditReplyActivity::class.java)
            myIntent.putExtra("topicTitle", mTopic.title)
            myIntent.putExtra("selectedSideTitle", mTopic.mySide!!.title)
            myIntent.putExtra("topicId", mTopicId)
            startActivity(myIntent)
        }





//        버튼이 눌리면 할 일을 변수에 담아서 저장.
//        TedPermission에서 권한별 할 일을 변수에 담아서 저장한것과 같은 논리

        val voteCode = View.OnClickListener {

//            it => View형태 => 눌린 버튼을 담고 있는 변수.

            val clickedSideTag = it.tag.toString()

            Log.d("눌린 버튼의 태그", clickedSideTag)

//            눌린 버튼의 태그를 Int로 바꿔서
//            토론 주제의 진영중 어떤 진영을 눌렀는지 가져오는 index로 활용
            val clickedSide = mTopic.sideList[clickedSideTag.toInt()]

            Log.d("투표하려는 진영 제목", clickedSide.title)

//            실제로 해당 진영에 투표하기
            ServerUtil.postRequestVote(mContext, clickedSide.id, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(json: JSONObject) {

//                    서버는 변경된 결과가 어떻게 되는지 다시 내려줌.
//                    이 응답에서, 토론 진행현황을 다시 파싱해서
//                    화면에 반영

                    val data = json.getJSONObject("data")
                    val topic = data.getJSONObject("topic")

//                    멤버변수로 있는 토픽을 갈아주자.

                    mTopic = Topic.getTopicFromJson(topic)

//                    화면에 mTopic의 데이터를 이용해서 반영

                    runOnUiThread {

                        setTopicDataToUi()
                    }


                }

            })

        }

//        두개의 투표하기 버튼이 눌리면 할 일을 모두 voteCode에 적힌 내용으로
        voteToFirstSideBtn.setOnClickListener(voteCode)
        voteToSecondSideBtn.setOnClickListener(voteCode)


    }

    override fun setValues() {

//        메인에서 넘겨준 id값을 멤버변수에 저장
//        만약 0이 저장되었다면 => 오류가 있는 상황으로 간주하자
        mTopicId = intent.getIntExtra("topicId", 0)

        if (mTopicId == 0) {
            Toast.makeText(mContext, "주제 상세 id에 문제가 있습니다.", Toast.LENGTH_SHORT).show()
        }

//        onResume에서 토론 진행현황을 서버에서 받아온다.
// 기존 서버 호출 코드 삭제

//       어댑터 초기화 -> 리스트뷰와 연결
        mReplyAdapter = ReplyAdapter(mContext, R.layout.reply_list_item, mReplyList)
        replyListView.adapter = mReplyAdapter
    }

    fun getTopicDetailFromServer() {

        ServerUtil.getRequestTopicDetail(mContext, mTopicId, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(json: JSONObject) {
// 의견목록을 한번 비웠다가 다시 파싱
                val data = json.getJSONObject("data")

                val topicObj = data.getJSONObject("topic")

//                서버에서 내려주는 주제 정보를
//                Topic 객체로 변환해서 멤버변수에 저장

                mTopic = Topic.getTopicFromJson(topicObj)
//             같이 불러오는 의견목록을 파싱해서 배열에 담자
                val replies = topicObj.getJSONArray("replies")
//              JSONArray를 하나씩 돌면서 Reply 형태로 바꿔서 목록에 추가
                for (i in 0 until replies.length()){
                    mReplyList.add(Reply.getReplyFromJson(replies.getJSONObject(i)))
                }
//                화면에 토론 관련 정보 표시
                runOnUiThread {

                    setTopicDataToUi()
                    // 댓글 목록을 불러왔다고 리스트뷰 어댑터에게 알림
                    mReplyAdapter.notifyDataSetChanged()

                }

            }

        })

    }

//    화면에 mTopic 기반으로 데이터 반영해주는 기능

    fun setTopicDataToUi() {
        topicTitleTxt.text = mTopic.title
        Glide.with(mContext).load(mTopic.imageUrl).into(topicImg)

//                    진영 정보도 같이 표시
        firstSideTitleTxt.text = mTopic.sideList[0].title
        secondSideTitleTxt.text = mTopic.sideList[1].title

        firstSideVoteCountTxt.text = "${mTopic.sideList[0].voteCount}표"
        secondSideVoteCountTxt.text = "${mTopic.sideList[1].voteCount}표"

//        내가 투표를 했는지 or 어느 진영에 했는지에 따라 버튼 UX 변경
//        투표 X : 두 버튼 모두 "투표하기"
//        첫번째 진영 투표 : 첫 버튼 "투표취소", 두번째 진영 "갈아타기"
//        그 외 : 두번째 진영 투표한걸로 처리

//        투표 한 진영이 몇번째 진영인지? 파악해야함


        if (mTopic.getMySideIndex() == -1) {
//            아직 투표 안한 경우
            voteToFirstSideBtn.text = "투표하기"
            voteToSecondSideBtn.text = "투표하기"
        }
        else if (mTopic.getMySideIndex() == 0) {
//            첫 진영에 투표한 경우
            voteToFirstSideBtn.text = "투표취소"
            voteToSecondSideBtn.text = "갈아타기"
        }
        else {
//            두번째 진영에 투표한 경우
            voteToFirstSideBtn.text = "갈아타기"
            voteToSecondSideBtn.text = "투표취소"
        }


    }
}