package com.example.colosseum_20200716.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Reply {
    var id = 0
    var content = ""
    lateinit var writer : User
    lateinit var selectedSide : Side
//  의견이 작성된 시간을 담는 변수
    val writtenDateTime = Calendar.getInstance()
    // 좋아요 / 싫어요 / 답글 갯수를 저장할 변수들
    var likecount = 0
    var dislikecount = 0
    var replyCount = 0
    //내가 좋아요를 눌렀는지 싫어요를 눌렀는지 저장할 변수들
    var myLike = false
    var myDislike = false
    companion object{
        //JSONObject 하나를 넣으면 의견내용을 파싱해서  Reply 로 리턴하는 기능
        fun getReplyFromJson(json: JSONObject) : Reply {

//            변환시켜줄 Topic 객체 생성
            val r = Reply()

//            만든 객체의 내용물들을 json을 이용해서 채우자
            r.id = json.getInt("id")
            r.content = json.getString("content")
            // 작성자 / 선택진영 => JSONObject를 받아서 곧바로 대입
            r.writer = User.getUserFromJson(json.getJSONObject("user"))

            r.selectedSide = Side.getSideFromJson(json.getJSONObject("selected_side"))
//          작성일시를 서버가 주는 내용을 분석해서 대입하자.

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            // 서버가 주는 내용을 변수로 저장
            val createdAtString = json.getString("created_at")

            // 멤버변수인 Calendar 변수에게 데이터 적용
            r.writtenDateTime.time = sdf.parse(createdAtString)

            // 핸드폰의 시간대와, 서버시간대의 시차를 구해서
            // 작성 일시의 시간값을 조정
            // 내 폰의 시간대가 어디 시간대인지 변수로 저장
            val myPhoneTimeZone = r.writtenDateTime.timeZone  // 한국폰 : 한국시간대
            // (서버랑) 몇시간 차이가 나는지 변수로 저장. -> 밀리초까지 계산된 시차 -> 시간 단위로 변경
            val timeOffset = myPhoneTimeZone.rawOffset / 1000 / 60 / 60
            // 게시글 작성시간을 timeOffset만큼 시간값을 더해주자.
            r.writtenDateTime.add(Calendar.HOUR, timeOffset)


            // 좋아요 / 싫어요 /  답글 갯수 실제 파싱
            r.likecount = json.getInt("like_count")
            r.dislikecount = json.getInt("dislike_count")
            r.replyCount = json.getInt("reply_count")
            // 좋아요 / 싫어요 여부도 파싱해서 저장
            r.myLike = json.getBoolean("my_like")
            r.myDislike = json.getBoolean("my_dislike")
            return r
        }
    }
}