package com.example.colosseum_20200716

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.colosseum_20200716.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }
        loginBtn.setOnClickListener {
            // 입력한 아이디와 비번을 받아서
            val inputEmail = emailEdt.text.toString()
            val inputPw = pwEdt.text.toString()

            // 서버에 전달해주고 응답 처리
            ServerUtil.postRequestLogin(mContext,inputEmail,inputPw, object :  ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
//              로그인 성공 / 실패 여부 => code에 있는 Int값으로 구별.
//              200: 로그인 성공
//              그 외의 숫자 : 로그인 실패
                    val codeNum = json.getInt("code")

                    if(codeNum == 200){
//              로그인 성공 = > 서버가 알려주는 토큰을 반영구 저장
                        //Toast.makeText(mContext,)
                        //json => data => token 스트링 추출
                        val data = json.getJSONObject("data")
                        val token = data.getString("token")
                        // 얻어낸 토큰을 저장

                        ContextUtil.setLoginUserToken(mContext, token)
                    }
                    else{
//              로그인 실패 => 토스트로 실패했다고 출력하자.
                        runOnUiThread {
                            val message = json.getString("message")
                            Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            })

        }
    }

    override fun setValues() {

    }


}