package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private EditText editTextID;
    private EditText editTextPassword;

    private Button buttonLogIn;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체

        editTextID = (EditText) findViewById(R.id.edittext_ID); // 입력받은 아이디
        editTextPassword = (EditText) findViewById(R.id.edittext_PW); // 입력받은 비밀번호

        buttonSignUp = (Button) findViewById(R.id.btn_signup); // 회원가입 버튼
        buttonLogIn = (Button) findViewById(R.id.btn_login); // 로그인 버튼

        // 회원가입 버튼 클릭시 회원가입 레이아웃으로 이동
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼 클릭시 로그인 확인
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextID.getText().toString().equals("data@firebase.com") && editTextPassword.getText().toString().equals("data123")) {
                    // 데이터 관리자 아이디로 로그인하는 경우
                    loginDataUser(editTextID.getText().toString(), editTextPassword.getText().toString());
                    Log.d(TAG, "DataManager Login Success");
                }
                else if (!editTextID.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                    loginUser(editTextID.getText().toString(), editTextPassword.getText().toString());
                } else if (editTextID.getText().toString().equals("")) {
                    // 아이디가 공백인 경우
                    Toast.makeText(LogInActivity.this, "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
                } else if (!editTextID.getText().toString().matches("[\\w]+@[\\w]+\\.[\\w]+")) {
                    // 아이디가 이메일 형식이 아닌 경우
                    Toast.makeText(LogInActivity.this, "아이디는 이메일 형식입니다.", Toast.LENGTH_LONG).show();
                } else if (editTextPassword.getText().toString().equals("")) {
                    // 비밀번호가 공백인 경우
                    Toast.makeText(LogInActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    // 아이디와 비밀번호가 공백인 경우
                    Toast.makeText(LogInActivity.this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 로그인 확인 성공시 지도 레이아웃으로 이동
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (!user.getEmail().equals("data@firebase.com") && user != null) { // 일반 사용자 로그인
                    Intent intent = new Intent(LogInActivity.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (user.getEmail().equals("data@firebase.com") && user != null) { // 데이터 관리자 로그인
                    Intent intent = new Intent(LogInActivity.this, DataManagerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "등록되지 않은 사용자입니다.", Toast.LENGTH_LONG).show();
                }
            }
        };
    }


    // 로그인 확인
    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);
                            Log.d(TAG, "Login Success");
                            Toast.makeText(LogInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Log.d(TAG, "Login Fail");
                            Toast.makeText(LogInActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 데이터 관리자 로그인 확인
    public void loginDataUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);
                            Log.d(TAG, "DataUser Login Success");
                            Toast.makeText(LogInActivity.this, "데이터 관리자 화면으로 이동합니다.", Toast.LENGTH_LONG).show();
                        } else {
                            // 로그인 실패
                            Log.d(TAG, "DataUser Login Fail");
                            Toast.makeText(LogInActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}