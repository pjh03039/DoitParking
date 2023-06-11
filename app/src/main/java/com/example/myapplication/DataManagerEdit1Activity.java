package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DataManagerEdit1Activity extends AppCompatActivity {
    private GoogleMap mMap;
    private DatabaseReference parkingDatabase;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchEditAdapter searcheditAdapter;
    private Handler handler = new Handler();
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamanageredit1);

        parkingDatabase = FirebaseDatabase.getInstance().getReference("Parking_Data");
        searchView = findViewById(R.id.search_edit_view);
        recyclerView = findViewById(R.id.recycler_edit_view);

        searcheditAdapter = new SearchEditAdapter(new ArrayList<>());
        searchView.clearFocus();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searcheditAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private ValueEventListener listener;
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ParkingClass> parkingList = new ArrayList<>();
                if (TextUtils.isEmpty(newText)) {
                    parkingList.clear();
                    searcheditAdapter.updateList(parkingList);
                    recyclerView.setAdapter(searcheditAdapter);
                    return true;
                }
                Query searchQuery= parkingDatabase.orderByChild("소재지도로명주소").startAt(newText).endAt(newText + "\uf8ff").limitToFirst(100);
                if (searchQuery.equals("")) {
                    searchQuery = parkingDatabase.orderByChild("소재지지번주소").startAt(newText).endAt(newText + "\uf8ff").limitToFirst(100);
                }
                if (listener != null) { // listener가 null이 아니면 이전 검색 쿼리의 결과를 취소함
                    searchQuery.removeEventListener(listener);
                    parkingList.clear();
                }
                listener = new ValueEventListener() { // 새로운 ValueEventListener 객체를 생성하여 추가
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        parkingList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String name = dataSnapshot.child("주차장명").getValue(String.class);
                            String roadAddress = dataSnapshot.child("소재지도로명주소").getValue(String.class);
                            String jibunAddress = dataSnapshot.child("소재지지번주소").getValue(String.class);
                            String parkingNumber = dataSnapshot.child("주차장관리번호").getValue(String.class);
                            String latitude = dataSnapshot.child("위도").getValue(String.class);
                            String longitude = dataSnapshot.child("경도").getValue(String.class);
                            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                                parkingList.add(new ParkingClass(name, roadAddress, jibunAddress, parkingNumber));
                            }
                        }
                        searcheditAdapter.updateList(parkingList);
                        recyclerView.setAdapter(searcheditAdapter);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "Failed to read value.", error.toException());
                    }
                };
                searchQuery.addListenerForSingleValueEvent(listener);
                return true;
            }
        });
    }
}
