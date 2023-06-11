package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private String user = currentUser.getEmail();

    private TextView parkinglot_name;
    private TextView parkinglot_address;
    private TextView parkingareaID;

    private Button buttonResvDate;
    private Button buttonrResvStartTime;
    private Button buttonResvEndTime;

    private Button buttonResvComplete;

    CalendarView calendar;

    TimePicker startTimePicker, endTimePicker;

    int selectY, selectM, selectD, selectStartH, selectStartMi, selectEndH, selectEndMi;

    private static String prkAdr;
    private static String prkName;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = db.child("Reservation");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        parkinglot_name = findViewById(R.id.parkinglot_nameText);
        parkinglot_name.setText(prkName);
        parkinglot_address = findViewById(R.id.parkinglot_placeText);
        parkinglot_address.setText(prkAdr);
        parkingareaID = findViewById(R.id.parkingarea_IDText);
        parkingareaID.setText(getIntent().getStringExtra("id"));

        calendar = findViewById(R.id.calendar);
        startTimePicker = findViewById(R.id.startTimePicker);
        endTimePicker = findViewById(R.id.endTimePicker);

        calendar.setVisibility(View.INVISIBLE);
        startTimePicker.setVisibility(View.INVISIBLE);
        endTimePicker.setVisibility(View.INVISIBLE);


        buttonResvDate = findViewById(R.id.resvDatePickBtn);
        buttonResvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(View.VISIBLE);
                startTimePicker.setVisibility(View.INVISIBLE);
                endTimePicker.setVisibility(View.INVISIBLE);
            }
        });

        buttonrResvStartTime = findViewById(R.id.resvStartTimePickBtn);
        buttonrResvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(View.INVISIBLE);
                startTimePicker.setVisibility(View.VISIBLE);
                endTimePicker.setVisibility(View.INVISIBLE);
            }
        });

        buttonResvEndTime = findViewById(R.id.resvEndTimePickBtn);
        buttonResvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(View.INVISIBLE);
                startTimePicker.setVisibility(View.INVISIBLE);
                endTimePicker.setVisibility(View.VISIBLE);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectY = year;
                selectM = month + 1;
                selectD = day;
            }
        });

        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                selectStartH = hour;
                selectStartMi = minute;
            }
        });

        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                selectEndH = hour;
                selectEndMi = minute;
            }
        });

        buttonResvComplete = findViewById(R.id.resvCompleteBtn);
        buttonResvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 예약 정보 가져오기
                String userId = user.substring(0, user.indexOf("@")).toLowerCase();
                int reservationNumber = 1;
                String parkingareaId = parkingareaID.getText().toString();
                String parkinglotPlace = parkinglot_address.getText().toString();
                String resvDate = selectY + "-" + selectM + "-" + selectD;
                String resvStartTime = selectStartH + ":" + selectStartMi;
                String resvEndTime = selectEndH + ":" + selectEndMi;


                // 중복 예약 확인
                checkReservation(userId, parkingareaId, resvStartTime, resvEndTime, new ReservationCallback() {
                    @Override
                    public void onResult(boolean isAvailable) {
                        if (isAvailable) {
                            // 예약 가능한 경우, 파이어베이스에 예약 정보 저장
                            saveReservation(userId, parkingareaId, parkinglotPlace, resvDate, resvStartTime, resvEndTime);
                            finish();
                            Toast.makeText(ReservationActivity.this, "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 중복된 예약이 있는 경우
                            Toast.makeText(ReservationActivity.this, "중복된 예약이 있습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
    }

    private void checkReservation(String userId, String parkingareaId, String resvStartTime, String resvEndTime, ReservationCallback callback) {
        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAvailable = true;

                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    ReservationClass reservation = reservationSnapshot.getValue(ReservationClass.class);

                    // 예약된 시간과 입력한 시간 사이에 중복이 있는지 확인
                    if (reservation != null && reservation.getParkingareaID().equals(parkingareaId)) {
                        String startTime = reservation.getResvStartTime();
                        String endTime = reservation.getResvEndTime();

                        if (isTimeOverlap(startTime, endTime, resvStartTime, resvEndTime)) {
                            isAvailable = false;
                            break;
                        }
                    }
                }

                callback.onResult(isAvailable);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }

    private void saveReservation(String userId, String parkingareaId, String parkinglotPlace, String resvDate, String resvStartTime, String resvEndTime) {
        DatabaseReference userRef = ref.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int parkingNumber = (int) snapshot.getChildrenCount() + 1;
                String reservationNumber = String.valueOf(parkingNumber);

                ReservationClass reservation = new ReservationClass(userId, reservationNumber, parkinglotPlace, parkingareaId, resvDate, resvStartTime, resvEndTime);
                userRef.child(reservationNumber).setValue(reservation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private boolean isTimeOverlap(String startTime1, String endTime1, String startTime2, String endTime2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");

            Date start1 = format.parse(startTime1);
            Date end1 = format.parse(endTime1);
            Date start2 = format.parse(startTime2);
            Date end2 = format.parse(endTime2);

            if (start1 != null && end1 != null && start2 != null && end2 != null) {
                // 예약 시간이 겹치는지 확인
                return (start1.before(end2) && end1.after(start2)) || (start2.before(end1) && end2.after(start1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private interface ReservationCallback {
        void onResult(boolean isAvailable);
    }


    public static void recevieReservation(String name, String adr) {
        prkName = name;
        prkAdr = adr;
    }
}
