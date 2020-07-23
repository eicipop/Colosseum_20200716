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
            return r
        }
    }
}