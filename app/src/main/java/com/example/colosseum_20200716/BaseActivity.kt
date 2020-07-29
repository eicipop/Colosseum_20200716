package com.example.colosseum_20200716


import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    val mContext = this

    abstract fun setupEvents()
    abstract fun setValues()

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

    }
}