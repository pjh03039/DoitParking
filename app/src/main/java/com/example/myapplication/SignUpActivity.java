package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDatabase;

    private EditText editTextId;
    private EditText editTextPassword;
    private EditText editTextCell;
    private RadioButton buttonCarType_Small;
    private RadioButton buttonCarType_Middle;
    private RadioButton buttonCarType_Large;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 사용자 인증
        userDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 실시간 데이터베이스

        editTextId = (EditText) findViewById(R.id.edittext_Id); // 입력받은 아이디
        editTextPassword = (EditText) findViewById(R.id.edittext_Password); // 입력받은 비밀번호
        editTextCell = (EditText) findViewById(R.id.editText_Cell); // 입력받은 전화번호
        buttonCarType_Small = (RadioButton) findViewById(R.id.buttonCarType_Small); // 선택 차량이 소형
        buttonCarType_Middle = (RadioButton) findViewById(R.id.buttonCarType_Middle); // 선택 차량이 일반
        buttonCarType_Large = (RadioButton) findViewById(R.id.buttonCarType_Large); // 선택 차량이 대형
        RadioGroup buttonCarType = findViewById(R.id.buttonCarType);

        buttonSignUp = (Button) findViewById(R.id.button_SignUp); // 회원가입 버튼

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextId.getText().toString().equals("") && !editTextPassword.getText().toString().equals("") && !editTextCell.getText().toString().equals("") && buttonCarType.getCheckedRadioButtonId() != -1) {
                    // 아이디, 비밀번호, 전화번호, 차종이 공백이 아닌 경우
                    createUser(editTextId.getText().toString(), editTextPassword.getText().toString(), editTextCell.getText().toString(), radioCheck(buttonCarType));
                } else if (editTextId.getText().toString().equals("")) {
                    // 아이디가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "계정을 입력하세요.", Toast.LENGTH_LONG).show();
                } else if (!editTextId.getText().toString().matches("[\\w]+@[\\w]+\\.[\\w]+")) {
                    // 아이디가 이메일 형식이 아닌 경우
                    Toast.makeText(SignUpActivity.this, "아이디는 이메일 형식이어야 합니다.", Toast.LENGTH_LONG).show();
                } else if (editTextPassword.getText().toString().equals("")) {
                    // 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                } else if (editTextPassword.getText().toString().length() < 6) {
                    // 비밀번호가 6자리 미만인 경우
                    Toast.makeText(SignUpActivity.this, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_LONG).show();
                } else if (buttonCarType.getCheckedRadioButtonId() == -1) {
                    // 차 종류가 선택되어있지 않은 경우
                    Toast.makeText(SignUpActivity.this, "차종을 선택하세요.", Toast.LENGTH_LONG).show();
                } else if (editTextCell.getText().toString().length() < 11) {
                    // 전화번호 형식이 올바르지 않은 경우
                    Toast.makeText(SignUpActivity.this, "전화번호 형식이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                } else if (editTextCell.getText().toString().equals("")) {
                    // 전화번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "전화번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonCarType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if(radioButton.isChecked()){
                    String CarType = radioButton.getText().toString();
                    // 선택된 라디오 버튼의 라벨값을 가져옴
                    Log.d("Selected RadioButton", CarType);
                }
            }
        });
    }

    private void createUser(String id, String password, String cell, String cartype) {
        firebaseAuth.createUserWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공 시
                            UserClass user = new UserClass(id, password, cell, cartype);
                            userDatabase.child("Users").child(id.substring(0, id.indexOf("@")).toLowerCase()).setValue(user); // 유저 데이터베이스에 ID(@ 뒷부분을 뺀)로 저장
                            Log.d(TAG, "Join Success");
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 회원가입 실패 시
                            Log.d(TAG, "Join Fail");
                            // 계정이 중복된 경우
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 선택된 라디오버튼 이름을 반환
    private String radioCheck(RadioGroup buttonCarType) {
        int checkedRadioButtonId = buttonCarType.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = findViewById(checkedRadioButtonId);
        String label = checkedRadioButton.getText().toString();
        return label;
    }
}
