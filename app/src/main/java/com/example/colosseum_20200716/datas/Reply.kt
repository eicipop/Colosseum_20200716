package com.example.colosseum_20200716.datas

class Reply {
    var id = 0
    var content = ""
    lateinit var writer : User
    lateinit var selectedSide : Side

    companion object{
        //JSONObject 하나를 넣으면 의견내용을 파싱해서  Reply 로 리턴하는 기능
    }
}