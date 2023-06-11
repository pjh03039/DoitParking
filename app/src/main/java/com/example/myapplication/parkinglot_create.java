package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityDatamanegerregister2Binding;

import java.util.Stack;

public class parkinglot_create extends AppCompatActivity implements View.OnClickListener {

    private ActivityDatamanegerregister2Binding binding;
    private String mapTxt; // 맵 텍스트파일
    private int askiiMapId;// 아스키 맵 뒤에 붙일 id
    private RelativeLayout mapLayout; //전역으로 선언해 onClick에서도 받게한다
    //private Parkinglot_button pastButton;//이전 버튼을 기억하고 현재 버튼 삭제시 이동할 버튼
    private Stack<Parkinglot_button> parkinglots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatamanegerregister2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //버튼 바인딩
        Button wall = binding.btnWall;
        Button space = binding.btnSpace;
        Button small = binding.btnSmall;
        Button middle = binding.btnMiddle;
        Button large = binding.btnLarge;
        Button entrance = binding.btnEntrance;
        Button next = binding.btnNext;
        Button cancle = binding.btnCancle;

        mapLayout = binding.mapLayout;

        //맵 초기화
        askiiMapId = 1;
        mapTxt = "";

        //버튼 초기화
        parkinglots = new Stack<>();

        //온클릭 리스너를 오버라이드 해서 버튼생성과 mapTxt에 맵들을 저장한다.
        wall.setOnClickListener(this);
        space.setOnClickListener(this);
        small.setOnClickListener(this);
        middle.setOnClickListener(this);
        large.setOnClickListener(this);
        entrance.setOnClickListener(this);
        next.setOnClickListener(this);
        cancle.setOnClickListener(this);

    }

    //onClick를 오버라이드로 각각의 버튼에 대응하는 작업을 처리한다.
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnWall:
                //벽 55
                // 반복되는거 최적화 해야됨
                mapTxt += "5500";

                //버튼 생성
                Parkinglot_button newWall = new Parkinglot_button(getApplicationContext());
                SettingButton(newWall, Color.GRAY, v);
                break;
            case R.id.btnSpace:
                //빈공간 66
                mapTxt += "6600";

                //버튼 생성
                Parkinglot_button newSpace = new Parkinglot_button(getApplicationContext());
                SettingButton(newSpace, Color.TRANSPARENT, v);
                break;
            case R.id.btnSmall:
                //소형 01
                mapTxt += ("01" + String.format("%02d",askiiMapId));

                //버튼 생성
                Parkinglot_button newSmall = new Parkinglot_button(getApplicationContext());
                SettingButton(newSmall, Color.RED, askiiMapId , v);

                askiiMapId++;
                break;
            case R.id.btnMiddle:
                //중형 02
                mapTxt += ("02" + String.format("%02d",askiiMapId));

                //버튼 생성
                Parkinglot_button newMiddle = new Parkinglot_button(getApplicationContext());
                SettingButton(newMiddle, Color.BLUE, askiiMapId , v);

                askiiMapId++;
                break;
            case R.id.btnLarge:
                //대형 03
                mapTxt += ("03" + String.format("%02d",askiiMapId));

                //버튼 생성
                Parkinglot_button newLarge = new Parkinglot_button(getApplicationContext());
                SettingButton(newLarge, Color.GREEN, askiiMapId , v);

                askiiMapId++;
                break;
            case R.id.btnEntrance:
                //출구 77
                mapTxt += "7700";

                //버튼 생성
                Parkinglot_button newEntrance = new Parkinglot_button(getApplicationContext());
                SettingButton(newEntrance, Color.BLACK, v);
                break;
            case R.id.btnNext:
                //개행을 해야됨 88
                mapTxt += "8800";

                if(parkinglots.empty() == false) {
                    //개행을 위해 true로 변경
                    parkinglots.peek().setIsNext(true);
                }
                break;
            case R.id.btnCancle:
                //이전 생성된 버튼과 mapTxt에 저장된걸 지워야됨
                if(mapTxt.length() != 0 ) {

                    if(askiiMapId > 1 &&  "00" != mapTxt.substring(mapTxt.length()-2, mapTxt.length())) { //이전 생성된 버튼이 주차장이 아니면 askiiMapId를 줄이면 안된다.
                        askiiMapId--;
                    }
                    mapTxt = mapTxt.substring(0, mapTxt.length() - 4);

                    if(parkinglots.empty() == false) {
                        Parkinglot_button button = parkinglots.pop();
                        //동적으로 생성된 버튼 삭제에 대한 코드 작성
                        mapLayout.removeView(button);
                    }
                }
                break;
        }
    }

    //일반 버튼을 세팅
    private void SettingButton(Parkinglot_button newButton,int color,  View v){
        newButton.setBackgroundColor(color);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                100,
                100
        );

        //버튼의 속성을 이전 버튼기반으로 정한다
        //첫번째 버튼인지 검사
        if(parkinglots.empty() == true) {
            params.leftMargin = 0;
            params.topMargin = 0;
        }else {
            //이전 버튼에 개행을 했는지 확인한다
            if(parkinglots.peek().getIsNext() == true) {
                params.topMargin += parkinglots.peek().getTopMargin() + 100;
                params.leftMargin = 0;
            }
            else {
                params.topMargin = parkinglots.peek().getTopMargin();
                params.leftMargin = parkinglots.peek().getLeftMargin() + 100;
            }
        }

        //현재 버튼의 마진 값을 기억한다
        newButton.setMargin(params.leftMargin, params.topMargin);

        //버튼의 상대적 위치 지정 규칙을 설정한다.
        params.addRule(RelativeLayout.BELOW, v.getId());

        //버튼을 뷰에 생성한다
        mapLayout.addView(newButton, params);

        //신규 버튼 생성
        parkinglots.push(newButton);
    }

    //주차 공간 버튼을 세팅
    private void SettingButton(Parkinglot_button newButton,int color, int askiimapId, View v){
        newButton.setBackgroundColor(color);
        newButton.setId(askiimapId);
        //버튼 크기
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                100,
                100
        );


        //버튼의 속성을 이전 버튼기반으로 정한다
        //첫번째 버튼인지 검사
        if(parkinglots.empty() == true) {
            params.leftMargin = 0;
            params.topMargin = 0;
        }else {
            //이전 버튼에 개행을 했는지 확인한다
            if(parkinglots.peek().getIsNext() == true) {
                params.topMargin += parkinglots.peek().getTopMargin() + 100;
                params.leftMargin = 0;
            }
            else {
                params.topMargin = parkinglots.peek().getTopMargin();
                params.leftMargin = parkinglots.peek().getLeftMargin() + 100;
            }
        }
        //현재 버튼의 마진 값을 기억한다
        newButton.setMargin(params.leftMargin, params.topMargin);

        //버튼의 상대적 위치 지정 규칙을 설정한다.
        params.addRule(RelativeLayout.BELOW, v.getId());

        //버튼을 뷰에 생성한다
        mapLayout.addView(newButton, params);

        //신규 버튼 생성
        parkinglots.push(newButton);
    }


}