package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataManagerRegister1Activity extends AppCompatActivity {
    private DatabaseReference parkingDatabase;
    private EditText parkinglot_place;
    private EditText parkinglot_latitude;
    private EditText parkinglot_longtitude;
    private EditText parkinglot_name;
    private EditText parkinglot_roadaddress;
    private EditText parkinglot_jibunaddress;
    private EditText parkinglot_number;
    private EditText parkinglot_separate;
    private EditText parkinglot_type;
    private EditText parkinglot_operday;
    private EditText parkinglot_phone;
    private Button buttonRegisterNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamanagerregister1);

        parkingDatabase = FirebaseDatabase.getInstance().getReference("Parking_Data");

        parkinglot_place = (EditText) findViewById(R.id.parkinglot_place);
        parkinglot_name = (EditText) findViewById(R.id.parkinglot_name);
        parkinglot_latitude = (EditText) findViewById(R.id.parkinglot_latitude);
        parkinglot_longtitude = (EditText) findViewById(R.id.parkinglot_longtitude);
        parkinglot_roadaddress = (EditText) findViewById(R.id.parkinglot_roadaddress);
        parkinglot_jibunaddress = (EditText) findViewById(R.id.parkinglot_jibunaddress);
        parkinglot_number = (EditText) findViewById(R.id.parkinglot_number);
        parkinglot_separate = (EditText) findViewById(R.id.parkinglot_separate);
        parkinglot_type = (EditText) findViewById(R.id.parkinglot_type);
        parkinglot_operday = (EditText) findViewById(R.id.parkinglot_operday);
        parkinglot_phone = (EditText) findViewById(R.id.parkinglot_phone);
        buttonRegisterNext = (Button) findViewById(R.id.btn_register_next);

        // 다음 버튼 클릭시 세번째 수정 레이아웃으로 이동
        buttonRegisterNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "주차장 관리번호가 중복되는지 확인합니다.", Toast.LENGTH_LONG).show();
                String parkinglotPlace = parkinglot_place.getText().toString();

                // 주차장 관리번호가 중복되는지 확인
                parkingDatabase.orderByChild("주차장관리번호").equalTo(parkinglotPlace).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 이미 같은 주차장 관리번호가 존재하는 경우
                            Toast.makeText(getApplicationContext(), "이미 등록된 주차장 관리번호입니다.", Toast.LENGTH_LONG).show();
                        } else {
                            // 새로운 노드에 데이터 저장
                            DatabaseReference newParkingRef = parkingDatabase.push(); // 새로운 노드 참조 생성
                            newParkingRef.child("주차장관리번호").setValue(parkinglot_place.getText().toString());
                            newParkingRef.child("위도").setValue(parkinglot_latitude.getText().toString());
                            newParkingRef.child("경도").setValue(parkinglot_longtitude.getText().toString());
                            newParkingRef.child("주차장명").setValue(parkinglot_name.getText().toString());
                            newParkingRef.child("소재지도로명주소").setValue(parkinglot_roadaddress.getText().toString());
                            newParkingRef.child("소재지지번주소").setValue(parkinglot_jibunaddress.getText().toString());
                            newParkingRef.child("주차구획수").setValue(parkinglot_number.getText().toString());
                            newParkingRef.child("주차장구분").setValue(parkinglot_separate.getText().toString());
                            newParkingRef.child("주차장유형").setValue(parkinglot_type.getText().toString());
                            newParkingRef.child("운영요일").setValue(parkinglot_operday.getText().toString());
                            newParkingRef.child("전화번호").setValue(parkinglot_phone.getText().toString());

                            // 데이터 저장 성공 시 다음 레이아웃으로 넘어감
                            Toast.makeText(getApplicationContext(), "주차장 정보를 등록중입니다.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DataManagerRegister1Activity.this, DataManagerRegister2Activity.class);
                            intent.putExtra("parkinglot_place", parkinglot_place.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 쿼리 실행이 취소된 경우 처리할 내용 작성
                    }
                });
            }
        });
    }
}
