package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DataManagerEdit2Activity extends AppCompatActivity {

    private DatabaseReference parkingDatabase;
    private EditText parkinglot_name;
    private EditText parkinglot_latitude;
    private EditText parkinglot_longtitude;
    private EditText parkinglot_roadaddress;
    private EditText parkinglot_jibunaddress;
    private EditText parkinglot_number;
    private EditText parkinglot_separate;
    private EditText parkinglot_type;
    private EditText parkinglot_operday;
    private EditText parkinglot_phone;

    private Button buttonEditNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamanageredit2);

        parkingDatabase = FirebaseDatabase.getInstance().getReference("Parking_Data");

        parkinglot_latitude = (EditText) findViewById(R.id.parkinglot_latitude);
        parkinglot_longtitude = (EditText) findViewById(R.id.parkinglot_longtitude);
        parkinglot_name = (EditText) findViewById(R.id.parkinglot_name);
        parkinglot_roadaddress = (EditText) findViewById(R.id.parkinglot_roadaddress);
        parkinglot_jibunaddress = (EditText) findViewById(R.id.parkinglot_jibunaddress);
        parkinglot_number = (EditText) findViewById(R.id.parkinglot_number);
        parkinglot_separate = (EditText) findViewById(R.id.parkinglot_separate);
        parkinglot_type = (EditText) findViewById(R.id.parkinglot_type);
        parkinglot_operday = (EditText) findViewById(R.id.parkinglot_operday);
        parkinglot_phone = (EditText) findViewById(R.id.parkinglot_phone);
        buttonEditNext = (Button) findViewById(R.id.btn_edit_next2);

        Intent intent = getIntent();
        String selectedParkingNo = intent.getStringExtra("selectedItem");
        Query query = parkingDatabase.orderByChild("주차장관리번호").equalTo(selectedParkingNo);
        Toast.makeText(getApplicationContext(), "주차장 정보를 불러오는 중입니다.", Toast.LENGTH_LONG).show();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // 해당 노드의 데이터 가져오기
                        parkinglot_latitude.setText(snapshot.child("위도").getValue(String.class));
                        parkinglot_longtitude.setText(snapshot.child("경도").getValue(String.class));
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        // ValueEventListener를 Query에 추가
        query.addListenerForSingleValueEvent(valueEventListener);

        // 다음 버튼 클릭시 세번째 수정 레이아웃으로 이동
        buttonEditNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "변경된 사항을 저장중입니다.", Toast.LENGTH_LONG);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                DatabaseReference selectedParkingRef = snapshot.getRef();
                                selectedParkingRef.child("위도").setValue(parkinglot_latitude.getText().toString());
                                selectedParkingRef.child("경도").setValue(parkinglot_longtitude.getText().toString());
                                selectedParkingRef.child("주차장명").setValue(parkinglot_name.getText().toString());
                                selectedParkingRef.child("소재지도로명주소").setValue(parkinglot_roadaddress.getText().toString());
                                selectedParkingRef.child("소재지지번주소").setValue(parkinglot_jibunaddress.getText().toString());
                                selectedParkingRef.child("주차구획수").setValue(parkinglot_number.getText().toString());
                                selectedParkingRef.child("주차장구분").setValue(parkinglot_separate.getText().toString());
                                selectedParkingRef.child("주차장유형").setValue(parkinglot_type.getText().toString());
                                selectedParkingRef.child("운영요일").setValue(parkinglot_operday.getText().toString());
                                selectedParkingRef.child("전화번호").setValue(parkinglot_phone.getText().toString(),
                                        new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    Intent intent = new Intent(DataManagerEdit2Activity.this, DataManagerEdit3Activity.class);
                                                    intent.getStringExtra(selectedParkingNo);
                                                    DataManagerEdit3Activity.recevieEdit3(selectedParkingNo);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Log.d(TAG, "저장하지 못함");
                                                }
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                    }
                });

            }
        });


    }

}
