package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private DatabaseReference parkingDatabase;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private Handler handler = new Handler();
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        parkingDatabase = FirebaseDatabase.getInstance().getReference("Parking_Data");
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_view);

        searchAdapter = new SearchAdapter(new ArrayList<>());
        searchView.clearFocus();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private ValueEventListener listener;
            @Override
            public boolean onQueryTextSubmit(String query) {
                Geocoder geocoder = new Geocoder(SearchActivity.this);
                List<Address> addresses = new ArrayList<>(10);
                ArrayList<ParkingClass> parkingList = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocationName(query, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    for (Address address : addresses) {
                        String name = query;
                        String roadAddress = address.getAddressLine(0);
                        String jibunAddress = "";
                        String latitude = String.valueOf(address.getLatitude());
                        String longitude = String.valueOf(address.getLongitude());
                        String prkplceNo = "";
                        parkingList.add(new ParkingClass(name, roadAddress, jibunAddress, latitude, longitude, prkplceNo));
                    }
                }
                // 검색 결과를 어댑터에 넘김
                searchAdapter.updateList(parkingList);
                recyclerView.setAdapter(searchAdapter);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ParkingClass> parkingList = new ArrayList<>();
                if (TextUtils.isEmpty(newText)) {
                    parkingList.clear();
                    searchAdapter.updateList(parkingList);
                    recyclerView.setAdapter(searchAdapter);
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
                            String latitude = dataSnapshot.child("위도").getValue(String.class);
                            String longitude = dataSnapshot.child("경도").getValue(String.class);
                            String prkplceNo = dataSnapshot.child("주차장관리번호").getValue(String.class);
                            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                                parkingList.add(new ParkingClass(name, roadAddress, jibunAddress, latitude, longitude, prkplceNo));
                            }
                        }
                        searchAdapter.updateList(parkingList);
                        recyclerView.setAdapter(searchAdapter);
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
