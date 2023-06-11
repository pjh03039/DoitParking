package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ReservationInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private String userEmail = currentUser.getEmail();

    private TextView textViewReservationInfo;
    private EditText etReservationNumber;
    private Button buttonResvDelete;

    private DatabaseReference reservationRef;
    private ValueEventListener reservationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationinfo);

        textViewReservationInfo = findViewById(R.id.textReservationInfo);
        etReservationNumber = findViewById(R.id.etReservationNumber);


        // 예약 정보를 가져올 Firebase 데이터베이스의 레퍼런스
        reservationRef = FirebaseDatabase.getInstance().getReference().child("Reservation");

        // 예약 정보 변경 시 호출되는 리스너
        reservationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 예약 정보 초기화
                StringBuilder reservationInfo = new StringBuilder();
                String user = userEmail.substring(0, userEmail.indexOf("@")).toLowerCase();

                // 예약 정보 확인
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    if (userId.equals(user)) {
                        for (DataSnapshot reservationSnapshot : userSnapshot.getChildren()) {
                            ReservationClass reservation = reservationSnapshot.getValue(ReservationClass.class);

                            if (reservation != null) {
                                reservationInfo.append("예약 번호: ")
                                        .append(reservation.getReservationNumber()).append("\n");
                                reservationInfo.append("주소: ")
                                        .append(reservation.getParkinglot_place()).append("\n");
                                reservationInfo.append("예약 날짜: ")
                                        .append(reservation.getResvDate()).append("\n");
                                reservationInfo.append("예약 시간: ")
                                        .append(reservation.getResvStartTime())
                                        .append(" ~ ")
                                        .append(reservation.getResvEndTime())
                                        .append("\n\n");

                            }
                        }
                        break;
                    }
                }

                buttonResvDelete = findViewById(R.id.buttonReservationDelete);
                buttonResvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String reservationNumber = etReservationNumber.getText().toString().trim();
                        deleteReservation(reservationNumber, user);
                    }
                });

                // 예약 정보를 텍스트뷰에 표시
                textViewReservationInfo.setText(reservationInfo.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 예약 정보 변경을 감지하기 위해 리스너를 추가
        reservationRef.addValueEventListener(reservationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 액티비티가 정지될 때 리스너를 제거
        if (reservationListener != null) {
            reservationRef.removeEventListener(reservationListener);
        }
    }

    private void deleteReservation(String reservationNumber, String user) {
        String inputReservationNumber = etReservationNumber.getText().toString().trim();

        if (inputReservationNumber.equals(reservationNumber)) {
            Query reservationQuery = reservationRef.child(user).orderByChild("reservationNumber").equalTo(reservationNumber);

            reservationQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // 해당 예약 번호를 가진 예약 정보를 찾음
                        for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                            ReservationClass reservation = reservationSnapshot.getValue(ReservationClass.class);

                            if (reservation != null && reservation.getReservationNumber().equals(reservationNumber)) {
                                reservationSnapshot.getRef().removeValue(); // 예약 정보 삭제
                            }
                        }

                        Toast.makeText(ReservationInfoActivity.this, "예약이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReservationInfoActivity.this, "해당 예약 번호를 가진 예약 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        } else {
            Toast.makeText(ReservationInfoActivity.this, "예약 번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}