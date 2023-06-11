package com.example.myapplication;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ParkingInfoActivity extends AppCompatActivity {

    private TextView parkinglot_name;
    private TextView parkinglot_roadaddress;
    private TextView parkinglot_jibunaddress;
    private TextView parkinglot_number;
    private TextView parkinglot_separate;
    private TextView parkinglot_type;
    private TextView parkinglot_operday;
    private TextView parkinglot_phone;
    private Button btn_reservation;

    private DatabaseReference prkDatabase;
    private static String parkingNm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkinglotinfo);

        parkinglot_name = (TextView) findViewById(R.id.parkinglot_name);
        parkinglot_roadaddress = (TextView) findViewById(R.id.parkinglot_roadaddress);
        parkinglot_jibunaddress = (TextView) findViewById(R.id.parkinglot_jibunaddress);
        parkinglot_number = (TextView) findViewById(R.id.parkinglot_number);
        parkinglot_separate = (TextView) findViewById(R.id.parkinglot_separate);
        parkinglot_type = (TextView) findViewById(R.id.parkinglot_type);
        parkinglot_operday = (TextView) findViewById(R.id.parkinglot_operday);
        parkinglot_phone = (TextView) findViewById(R.id.parkinglot_phone);
        btn_reservation = (Button) findViewById(R.id.btn_reservation);

        prkDatabase = FirebaseDatabase.getInstance().getReference("Parking_Data"); // Firebase Database

        // prkNum을 주차장관리번호 속성으로 가진 튜플을 찾는 쿼리
        Toast.makeText(getApplicationContext(), "주차장 정보를 불러오는 중입니다.", Toast.LENGTH_LONG).show();
        Query query = prkDatabase.orderByChild("주차장관리번호").equalTo(parkingNm);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 원하는 속성 값 추출
                    parkinglot_name.setText(snapshot.child("주차장명").getValue(String.class));
                    parkinglot_roadaddress.setText(snapshot.child("소재지도로명주소").getValue(String.class));
                    parkinglot_jibunaddress.setText(snapshot.child("소재지지번주소").getValue(String.class));
                    parkinglot_number.setText(snapshot.child("주차구획수").getValue(String.class));
                    parkinglot_separate.setText(snapshot.child("주차장구분").getValue(String.class));
                    parkinglot_type.setText(snapshot.child("주차장유형").getValue(String.class));
                    parkinglot_operday.setText(snapshot.child("운영요일").getValue(String.class));
                    parkinglot_phone.setText(snapshot.child("전화번호").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 예외 처리 추가 예정
            }
        });


        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParkingInfoActivity.this, ParkingDesignReservationAcitivty.class);
                ReservationActivity.recevieReservation(parkinglot_name.getText().toString(), parkinglot_roadaddress.getText().toString());
                ParkingDesignReservationAcitivty.recevieParkingDesignReservationData(parkingNm);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void recevieParkingInfoData(String data) {
        parkingNm = data;
    }
}