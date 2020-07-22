package com.example.colosseum_20200716.datas

import org.json.JSONObject

class Reply {
    var id = 0
    var content = ""
    lateinit var writer : User
    lateinit var selectedSide : Side

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

            return r
        }
    }
}