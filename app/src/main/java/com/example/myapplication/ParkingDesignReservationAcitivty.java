package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityParkingdesignreservationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Stack;

//주차장 도면을 받아와 생성하고 예약화면으로 이동하는 엑티비티
public class ParkingDesignReservationAcitivty extends AppCompatActivity {
    private ActivityParkingdesignreservationBinding binding;
    private String askiiMap;//도면 생성을 위한 아스키 맵
    private RelativeLayout mapLayout;
    private Stack<Parkinglot_button> parkinglots;

    private DatabaseReference parkingDatabase;
    private static String parkingNm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParkingdesignreservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        parkingDatabase = FirebaseDatabase.getInstance().getReference("Parking_Data");

        mapLayout = binding.mapLayout;

        //버튼 초기화
        parkinglots = new Stack<>();

        Toast.makeText(getApplicationContext(), "주차장 도면을 불러오는 중입니다.", Toast.LENGTH_LONG).show();
        Query query = parkingDatabase.orderByChild("주차장관리번호").equalTo(parkingNm);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    askiiMap = childSnapshot.child("텍스트맵").getValue(String.class);
                    if(askiiMap != null) {
                        createPKL(askiiMap);
                    } else {
                        Toast.makeText(getApplicationContext(), "도면을 준비중입니다. 다른곳을 이용해주세요.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 쿼리가 취소되었을 때의 처리를 수행합니다.
            }
        });
    }

    //받아온 아스키맵으로 맵 생성
    private void createPKL(String askiiMap){
        String space; // 공간에 대한 정보
        int id; // id에 대한 정보

        //4개씩 읽는다
        for(int i = 0; i < askiiMap.length(); i += 4){
            space = askiiMap.substring(i,i+2);
            id = Integer.parseInt(askiiMap.substring(i+2, i+4));

            switch (space){
                case "55":
                    // 벽
                    Parkinglot_button newWall = new Parkinglot_button(getApplicationContext());
                    SettingButton(newWall, Color.GRAY);
                    // 버튼 클릭 메소드 생성 후 메소드 안에서 해당 주차구획아이디 넘기기
                    break;
                case "66":
                    // 빈공간
                    Parkinglot_button newSpace = new Parkinglot_button(getApplicationContext());
                    SettingButton(newSpace, Color.TRANSPARENT);
                    break;
                case "77":
                    //출구
                    Parkinglot_button newEntrance = new Parkinglot_button(getApplicationContext());
                    SettingButton(newEntrance, Color.BLACK);

                    //테두리추가
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setStroke(10, Color.BLACK);
                    newEntrance.setBackground(drawable);

                    break;
                case "88":
                    //개행
                    if(parkinglots.empty() == false) {
                        //개행을 위해 true로 변경
                        parkinglots.peek().setIsNext(true);
                    }
                    break;
                case "01":
                    // 소형
                    Parkinglot_button newSmall = new Parkinglot_button(getApplicationContext());
                    SettingButton(newSmall, Color.RED, id);

                    //화면전환
                    int finalId_s = id;
                    newSmall.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra("id", String.valueOf(finalId_s));
                            startActivity(intent);
                            finish();
                        }

                    });
                    break;
                case "02":
                    // 중형
                    Parkinglot_button newMiddle = new Parkinglot_button(getApplicationContext());
                    SettingButton(newMiddle, Color.BLUE, id);

                    //화면전환
                    int finalId_m = id;
                    newMiddle.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra("id", String.valueOf(finalId_m));
                            startActivity(intent);
                            finish();
                        }

                    });
                    break;
                case "03":
                    // 대형
                    Parkinglot_button newLarge = new Parkinglot_button(getApplicationContext());
                    SettingButton(newLarge, Color.GREEN, id);

                    //화면전환
                    int finalId_l = id;
                    newLarge.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra("id", String.valueOf(finalId_l));
                            startActivity(intent);
                            finish();
                        }

                    });
                    break;
            }
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

    public static void recevieParkingDesignReservationData(String data) {
        parkingNm = data;
    }
}

//벽 55, 빈공간 66, 출구 77, 개행 88, 소형 01, 중형 02, 대형 03
// id가 00 이면 주차 공간이 아님  ex) 9900