package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatButton;

public class Parkinglot_button extends AppCompatButton { //gradle의 compoilesdk 33으로 맞춰야 적용됨

    //개행검사용 boolean
    private boolean isNext;

    //자신의 마진을 기억한다
    private int leftMargin;
    private int topMargin;

    public Parkinglot_button(Context context){
        super(context);
        init();

        leftMargin = 0;
        topMargin = 0;
        isNext = false;
    }

    public Parkinglot_button(Context context, AttributeSet attrs){
        super(context);
        init();

        leftMargin = 0;
        topMargin = 0;
        isNext = false;
    }

    //버튼의 초기 속성 설정
    private void init(){
        setTextColor(Color.BLACK);
        setWidth(R.drawable.parkinglot_button);
        setHeight(R.drawable.parkinglot_button);
    }



    //버튼의 마진 세팅
    public void setMargin(int left, int top){
        leftMargin = left;
        topMargin = top;
    }
    public int getLeftMargin(){return leftMargin;}
    public int getTopMargin(){return topMargin;}

    public void setIsNext(boolean nextLine){ isNext = nextLine;}
    public boolean getIsNext(){return isNext;}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
