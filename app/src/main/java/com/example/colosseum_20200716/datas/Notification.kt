package com.example.colosseum_20200716.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Notification {
    var id = 0
    var title =""
    lateinit var writer : User
    lateinit var selectedSide : Side
// 알림이 발생한 시간을 기록할 Calendar 변수
    val createAtCal = Calendar.getInstance()

    companion object{
//  SimpleDateFormat은 고정양식이므로 한번만 만들고 재활요

        fun getNotificationFromJson(json : JSONObject) : Notification{
            val n = Notification()
            // 사용자 정보파싱하는 코드

            //            만든 객체의 내용물들을 json을 이용해서 채우자
            n.id = json.getInt("id")
            n.title = json.getString("title")
            //u.createAtCal = json.getString("created_at")
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            // 서버가 주는 내용을 변수로 저장
            val createdAtString = json.getString("created_at")

            // 멤버변수인 Calendar 변수에게 데이터 적용
            n.createAtCal.time = sdf.parse(createdAtString)

//            sides 배열에 들어있는, 진영 선택 정보도 넣어줘야함.
            val myPhoneTimeZone = n.createAtCal.timeZone  // 한국폰 : 한국시간대
            // (서버랑) 몇시간 차이가 나는지 변수로 저장. -> 밀리초까지 계산된 시차 -> 시간 단위로 변경
            val timeOffset = myPhoneTimeZone.rawOffset / 1000 / 60 / 60
            // 게시글 작성시간을 timeOffset만큼 시간값을 더해주자.
            n.createAtCal.add(Calendar.HOUR, timeOffset)


            // 좋아요 / 싫어요 /  답글 갯수 실제 파싱
            n.id = json.getInt("id")
            n.title = json.getString("title")

            return  n
        }

    }
}