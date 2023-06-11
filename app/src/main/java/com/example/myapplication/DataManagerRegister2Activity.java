package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityDatamanegerregister2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Stack;

public class DataManagerRegister2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRegisterComplete;
    private ActivityDatamanegerregister2Binding binding;
    private String mapTxt; // 맵 텍스트파일
    private int askiiMapId;// 아스키 맵 뒤에 붙일 id
    private RelativeLayout mapLayout; //전역으로 선언해 onClick에서도 받게한다
    private Stack<Parkinglot_button> parkinglots;
    Intent intent = getIntent();
    private DatabaseReference parkingDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatamanegerregister2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        parkingDatabase = FirebaseDatabase.getInstance().getReference("Parking_Data");

        //버튼 바인딩
        Button wall = binding.btnWall;
        Button space = binding.btnSpace;
        Button small = binding.btnSmall;
        Button middle = binding.btnMiddle;
        Button large = binding.btnLarge;
        Button entrance = binding.btnEntrance;
        Button next = binding.btnNext;
        Button cancle = binding.btnCancle;
        Button buttonRegisterComplete = binding.buttonRegisterComplete;

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

        Intent intent = getIntent();
        String parkinglot_place = intent.getStringExtra("parkinglot_place");
        Query query = parkingDatabase.orderByChild("주차장관리번호").equalTo(parkinglot_place);

        if (intent != null && intent.hasExtra("parkinglot_place")) {
            buttonRegisterComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                childSnapshot.getRef().child("텍스트맵").setValue(mapTxt);
                            }
                            Toast.makeText(getApplicationContext(), "주차장 도면을 등록중입니다.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "텍스트맵 추가 실패");
                        }
                    });
                }
            });
        }
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
                SettingButton(newWall, Color.GRAY);
                break;
            case R.id.btnSpace:
                //빈공간 66
                mapTxt += "6600";

                //버튼 생성
                Parkinglot_button newSpace = new Parkinglot_button(getApplicationContext());
                SettingButton(newSpace, Color.TRANSPARENT);
                break;
            case R.id.btnSmall:
                //소형 01
                mapTxt += ("01" + String.format("%02d",askiiMapId));

                //버튼 생성
                Parkinglot_button newSmall = new Parkinglot_button(getApplicationContext());
                SettingButton(newSmall, Color.RED, askiiMapId);

                askiiMapId++;
                break;
            case R.id.btnMiddle:
                //중형 02
                mapTxt += ("02" + String.format("%02d",askiiMapId));

                //버튼 생성
                Parkinglot_button newMiddle = new Parkinglot_button(getApplicationContext());
                SettingButton(newMiddle, Color.BLUE, askiiMapId);

                askiiMapId++;
                break;
            case R.id.btnLarge:
                //대형 03
                mapTxt += ("03" + String.format("%02d",askiiMapId));

                //버튼 생성
                Parkinglot_button newLarge = new Parkinglot_button(getApplicationContext());
                SettingButton(newLarge, Color.GREEN, askiiMapId);

                askiiMapId++;
                break;
            case R.id.btnEntrance:
                //출구 77
                mapTxt += "7700";

                //버튼 생성
                Parkinglot_button newEntrance = new Parkinglot_button(getApplicationContext());
                SettingButton(newEntrance, Color.BLACK);

                //테두리추가
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(10, Color.BLACK);
                newEntrance.setBackground(drawable);

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

    //공간 세팅
    private void SettingButton(Parkinglot_button newButton,int color){
        newButton.setBackgroundColor(color);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setStroke(3, Color.BLACK);
        //버튼 크기
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                200,
                200
        );

        //버튼의 속성을 이전 버튼기반으로 정한다
        //첫번째 버튼인지 검사
        if(parkinglots.empty() == true) {
            params.leftMargin = 0;
            params.topMargin = 0;
        }else {
            //이전 버튼에 개행을 했는지 확인한다
            if(parkinglots.peek().getIsNext() == true) {
                params.topMargin += parkinglots.peek().getTopMargin() + 200;
                params.leftMargin = 0;
            }
            else {
                params.topMargin = parkinglots.peek().getTopMargin();
                params.leftMargin = parkinglots.peek().getLeftMargin() + 200;
            }
        }
        //현재 버튼의 마진 값을 기억한다
        newButton.setMargin(params.leftMargin, params.topMargin);

        //버튼의 상대적 위치 지정 규칙을 설정한다.
        params.addRule(RelativeLayout.BELOW);

        //버튼을 뷰에 생성한다
        mapLayout.addView(newButton, params);

        //신규 버튼 생성
        parkinglots.push(newButton);
    }


    //주차 공간 버튼을 세팅
    private void SettingButton(Parkinglot_button newButton,int color, int Id){
        newButton.setBackgroundColor(color);
        newButton.setId(Id);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setStroke(1, Color.BLACK);

        newButton.setText(Integer.toString(Id));
        newButton.setTextColor(Color.BLACK);
        newButton.setTextSize(20);
        newButton.setGravity(Gravity.CENTER);


        //버튼 크기
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                200,
                200
        );


        //버튼의 속성을 이전 버튼기반으로 정한다
        //첫번째 버튼인지 검사
        if(parkinglots.empty() == true) {
            params.leftMargin = 0;
            params.topMargin = 0;
        }else {
            //이전 버튼에 개행을 했는지 확인한다
            if(parkinglots.peek().getIsNext() == true) {
                params.topMargin += parkinglots.peek().getTopMargin() + 200;
                params.leftMargin = 0;
            }
            else {
                params.topMargin = parkinglots.peek().getTopMargin();
                params.leftMargin = parkinglots.peek().getLeftMargin() + 200;
            }
        }
        //현재 버튼의 마진 값을 기억한다
        newButton.setMargin(params.leftMargin, params.topMargin);

        //버튼의 상대적 위치 지정 규칙을 설정한다.
        params.addRule(RelativeLayout.BELOW);

        newButton.setBackground(drawable);
        //버튼을 뷰에 생성한다
        mapLayout.addView(newButton, params);

        //신규 버튼 생성
        parkinglots.push(newButton);
    }
}
