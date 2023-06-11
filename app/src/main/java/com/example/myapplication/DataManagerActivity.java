package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DataManagerActivity extends AppCompatActivity {

    private Button buttonRegister;
    private Button buttonEdit;
    private Button buttonLogout;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamanager);

        buttonRegister = (Button) findViewById(R.id.btn_register);
        buttonEdit = (Button) findViewById(R.id.btn_edit);
        buttonLogout = (Button) findViewById(R.id.btn_logout);

        // 등록 버튼 클릭시 등록 레이아웃으로 이동
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataManagerActivity.this, DataManagerRegister1Activity.class);
                startActivity(intent);
            }
        });

        // 수정 버튼 클릭시 수정 레이아웃으로 이동
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataManagerActivity.this, DataManagerEdit1Activity.class);
                startActivity(intent);
            }
        });

        // 데이터 관리자 로그아웃
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "데이터 관리자 로그아웃 되었습니다", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
