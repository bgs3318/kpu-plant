package kr.ac.kpu.se2018158037.tensor_test1;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button login_button,  Register;
    //TextView mResigettxt;
    EditText login_email, login_password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth =  FirebaseAuth.getInstance();
        //버튼 등록하기
        login_button = findViewById(R.id.login_button);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        Register = findViewById(R.id.join_button);

        //가입 버튼이 눌리면
        Register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        //로그인 버튼이 눌리면
        login_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String email = login_email.getText().toString().trim();
                String pwd = login_password.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){ //로그인 성공

                                    Toast.makeText(LoginActivity.this,"환영합니다",Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("value1",email);
                                    startActivity(intent);

                                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    //startActivity(intent);

                                }else{
                                    Toast.makeText(LoginActivity.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}