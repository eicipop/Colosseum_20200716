package com.example.colosseum_20200716


import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    val mContext = this

    abstract fun setupEvents()
    abstract fun setValues()

//  액션바 xml에서 만들어둔 뷰들을 멤버변수로 만들어주자.
//  BaseActivity를 상속받는 모든 액티비티들이 이 변수들을 상속받게 된다.

    lateinit var noticationBtn : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      BaseActivity를 상속받는 모든 액티비티는
        // onCreate에서 커스텀 액션바 설정을 하도록 하자.
        // 액션바가 있는지 확인하고 실행
        supportActionBar?.let{
//      supportActionBar가 null이 아닐때 실행 할 코드 블록 - let으로 지정
            setCustomActionBar()
        }
    }

    //  액션바를 커스텀으로 바꿔주는 기능
    fun setCustomActionBar() {
        // 액션바가 절대null이 아니라고 별개의 변수에 담자
        val myActionBar = supportActionBar!!
//  액션바를 커스텀 액션바를 쓸 수 있도록 세팅
        myActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        myActionBar.setCustomView(R.layout.custom_action_bar)

//  액션바 뒤의 기본색 제거 -> 액션바를 들고 있는 툴바의 좌우여백을 0으로 없애자.
        val parentToolbar = myActionBar.customView.parent as androidx.appcompat.widget.Toolbar
        parentToolbar.setContentInsetsAbsolute(0,0)

//  액션바 XML에 있는 뷰들을 코틀린에서 사용할 수 있도록 연결
        noticationBtn = myActionBar.customView.findViewById(R.id.notificationBtn)

    }
}