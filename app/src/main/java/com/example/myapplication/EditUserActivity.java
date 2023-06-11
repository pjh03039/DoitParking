package com.example.myapplication;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("Users");

    private TextView userIdTextView;
    private EditText newEditTextPassword;
    private EditText newEditTextCell;
    private RadioGroup newbuttonCarType;
    private RadioButton newbuttonCarType_Small;
    private RadioButton newbuttonCarType_Middle;
    private RadioButton newbuttonCarType_Large;

    private Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);

        userIdTextView = findViewById(R.id.textview_Id);
        userIdTextView.setText(user.getEmail());

        newEditTextPassword = (EditText) findViewById(R.id.newedittext_Password);

        newEditTextCell = (EditText) findViewById(R.id.neweditText_Cell);;

        newbuttonCarType_Small = (RadioButton) findViewById(R.id.newbuttonCarType_Small);
        newbuttonCarType_Middle = (RadioButton) findViewById(R.id.newbuttonCarType_Middle);
        newbuttonCarType_Large = (RadioButton) findViewById(R.id.newbuttonCarType_Large);
        newbuttonCarType = findViewById(R.id.newbuttonCarType);

        buttonEdit = (Button) findViewById(R.id.button_Edit); // 정보수정 버튼
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newEditTextPassword.getText().toString().length() < 6) {
                    Toast.makeText(EditUserActivity.this, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                } else if (newbuttonCarType.getCheckedRadioButtonId() == -1) {
                    // 차 종류가 선택되어있지 않은 경우
                    Toast.makeText(EditUserActivity.this, "차종을 선택하세요.", Toast.LENGTH_LONG).show();
                } else if (newEditTextCell.getText().toString().equals("")) {
                    // 전화번호가 공백인 경우
                    Toast.makeText(EditUserActivity.this, "전화번호를 입력하세요.", Toast.LENGTH_LONG).show();
                } else if (newEditTextCell.getText().toString().length() < 11) {
                    // 전화번호 형식이 올바르지 않은 경우
                    Toast.makeText(EditUserActivity.this, "전화번호 형식이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                } else {
                    editUserpassword(newEditTextPassword.getText().toString()); // 비밀번호 변경
                    editUsercartype(newbuttonCarType);
                    editUsercell(newEditTextCell.getText().toString());
                    Toast.makeText(EditUserActivity.this, "정보 수정 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    // 비밀번호 변경
    public void editUserpassword(String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
        userDatabase.child(user.getEmail().substring(0, user.getEmail().indexOf("@")).toLowerCase()).child("userPW").setValue(newPassword);
    }

    // 차종 변경
    public void editUsercartype(RadioGroup radioGroup) {
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioButtonId);
        userDatabase.child(user.getEmail().substring(0, user.getEmail().indexOf("@")).toLowerCase()).child("userCarType").setValue(radioButton.getText().toString());
    }

    // 차종 변경
    public void editUsercell(String newCell) {
        userDatabase.child(user.getEmail().substring(0, user.getEmail().indexOf("@")).toLowerCase()).child("userCell").setValue(newCell);
    }
}
