package com.example.colosseum_20200716.datas

import org.json.JSONObject

class User {
    var id = 0
    var email = ""
    var nickName = ""

    companion object{

        fun getUserFromJson(json : JSONObject) : User{
            val u = User()
            // 사용자 정보파싱하는 코드

            //            만든 객체의 내용물들을 json을 이용해서 채우자
            u.id = json.getInt("id")
            u.email = json.getString("email")
            u.nickName = json.getString("nick_name")

//            sides 배열에 들어있는, 진영 선택 정보도 넣어줘야함.


            return  u
        }

    }
}