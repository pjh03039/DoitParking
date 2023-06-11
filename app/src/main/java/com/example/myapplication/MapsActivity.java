package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView imageView;
    private List<LatLng> latLngList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> numberList = new ArrayList<>();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int LOCATION_REQUEST_CODE = 101;
    private Map<String, Marker> markerMap;  // 마커를 저장할 맵

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // 맵 프래그먼트 초기화
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        // 지도 초기화
        mapFragment.getMapAsync(this);
        markerMap = new HashMap<>();  // 맵 초기화

        // 툴바 초기화
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 네비게이션 드로어 초기화
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        // 햄버거 버튼 클릭시
        NavigationView Menu_menu = findViewById(R.id.menu_menu);
        Menu_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.menu_edit) { // 정보수정버튼 클릭시
                    Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu_reservation) {
                    Intent intent = new Intent(getApplicationContext(), ReservationInfoActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu_logout) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다", Toast.LENGTH_LONG).show();
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // 검색 아이콘 클릭시
        imageView = findViewById(R.id.search_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });



    }

    private void InsertParkingData(List<LatLng> latLngList) {
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Parking_Data");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String strlat = snapshot.child("위도").getValue(String.class);
                        String strlng = snapshot.child("경도").getValue(String.class);
                        String parknumber = snapshot.child("주차장관리번호").getValue(String.class);
                        String parkname = snapshot.child("주차장이름").getValue(String.class);
                        if (!strlng.equals("") && !strlat.equals("")) {
                            double lat = Double.parseDouble(strlat);
                            double lng = Double.parseDouble(strlng);
                            LatLng parkingLocation = new LatLng(lat, lng);
                            latLngList.add(parkingLocation);
                            nameList.add(parkname);
                            numberList.add(parknumber);
                        }
                    }
                    CreateMarker(latLngList, nameList, numberList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "주차 데이터를 불러오는데 실패했습니다: " + databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "주차 데이터를 불러오는데 실패했습니다.");
        }
    }

    private void CreateMarker(List<LatLng> latLngList, List<String> nameList, List<String> numberList) {
        List<Marker> markerList = new ArrayList<>();

        for (int i = 0; i < latLngList.size(); i++) {
            LatLng latLng = latLngList.get(i);
            String name = nameList.get(i);
            String number = numberList.get(i);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(name)
                    .snippet(number); // 주차장 관리 번호를 snippet으로 추가

            Marker marker = mMap.addMarker(markerOptions);
            markerList.add(marker);
        }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng cameraPosition = mMap.getCameraPosition().target;
                showMarkersWithinRadius(cameraPosition, markerList);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String parkingNumber = marker.getSnippet();
                ParkingInfoActivity.recevieParkingInfoData(parkingNumber);
                Intent intent = new Intent(MapsActivity.this, ParkingInfoActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }


    private void showMarkersWithinRadius(LatLng cameraPosition, List<Marker> markerList) {
        // 반경 2km 이내의 마커만 표시하도록 필터링합니다.
        List<Marker> markersToShow = new ArrayList<>();
        double radius = 2000; // 반경 2km
        for (Marker marker : markerList) {
            LatLng markerPosition = marker.getPosition();
            float distance = calculateDistance(cameraPosition, markerPosition);
            if (distance <= radius && mMap.getCameraPosition().zoom >= 15) {
                markersToShow.add(marker);
            }
        }

        // 모든 마커를 숨깁니다.
        for (Marker marker : markerList) {
            marker.setVisible(false);
        }

        // 반경 2km 이내의 마커만 보이도록 설정합니다.
        for (Marker marker : markersToShow) {
            marker.setVisible(true);
        }
    }


    private float calculateDistance(LatLng start, LatLng end) {
        Location startLocation = new Location("");
        startLocation.setLatitude(start.latitude);
        startLocation.setLongitude(start.longitude);

        Location endLocation = new Location("");
        endLocation.setLatitude(end.latitude);
        endLocation.setLongitude(end.longitude);

        return startLocation.distanceTo(endLocation);
    }

    // 지도 띄우기
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // 위치 권한이 있는지 확인
        if (checkLocationPermission()) {
            InsertParkingData(latLngList);
            // UiSettings 가져오기
            UiSettings uiSettings = mMap.getUiSettings();
            // 내 위치 버튼 활성화
            uiSettings.setMyLocationButtonEnabled(true);
        } else {
            requestLocationPermission(); // 권한 재요청
        }
    }

    // 위치 권한이 있는지 확인
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation();
            // 위치 정보 획득
            //loadParkingMarkers();
            return true;
        }
        return false;
    }

    // 위치 권한 요청
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    // 위치 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            } else {
                showPermissionDeniedDialog();
            }
        }
    }

    // 위치 정보 획득
    private void getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        }
    }

    // 위치 권한 거부 대화상자 표시
    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한 거부")
                .setMessage("위치 권한이 거부되었습니다. 앱을 사용하려면 위치 권한을 허용해야 합니다.")
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 설정 화면으로 이동
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 앱 종료
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
